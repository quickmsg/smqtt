package io.github.quickmsg.core.websocket;

import io.github.quickmsg.common.Receiver;
import io.github.quickmsg.common.channel.MqttChannel;
import io.github.quickmsg.core.mqtt.MetricChannelHandler;
import io.github.quickmsg.core.mqtt.MqttConfiguration;
import io.github.quickmsg.core.mqtt.MqttReceiveContext;
import io.github.quickmsg.core.ssl.AbstractSslHandler;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.tcp.TcpServer;
import reactor.util.context.ContextView;

/**
 * @author luxurong
 */
@Slf4j
public class WebSocketMqttReceiver extends AbstractSslHandler implements Receiver {

    @Override
    public Mono<DisposableServer> bind() {
        return Mono.deferContextual(contextView -> Mono.just(this.newTcpServer(contextView)))
                .flatMap(view -> view.bind().cast(DisposableServer.class));
    }

    private TcpServer newTcpServer(ContextView context) {
        MqttReceiveContext receiveContext = context.get(MqttReceiveContext.class);
        MqttConfiguration mqttConfiguration = receiveContext.getConfiguration();
        TcpServer server = TcpServer.create();
        if (mqttConfiguration.getSsl()) {
            server.secure(sslContextSpec -> this.secure(sslContextSpec, mqttConfiguration));
        }
        return server
                .port(mqttConfiguration.getWebSocketPort())
                .doOnBind(mqttConfiguration.getTcpServerConfig())
                .wiretap(mqttConfiguration.getWiretap())
                .childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(mqttConfiguration.getLowWaterMark(), mqttConfiguration.getHighWaterMark()))
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .runOn(receiveContext.getLoopResources())
                .doOnConnection(connection -> {
                    connection.addHandlerLast(new MetricChannelHandler())
                            .addHandlerLast(new HttpServerCodec())
                            .addHandlerLast(new HttpObjectAggregator(65536))
                            .addHandlerLast(new WebSocketServerProtocolHandler(mqttConfiguration.getWebSocketPath(), "mqtt, mqttv3.1, mqttv3.1.1"))
                            .addHandlerLast(new WebSocketFrameToByteBufDecoder())
                            .addHandlerLast(new ByteBufToWebSocketFrameEncoder())
                            .addHandlerLast(new MqttDecoder())
                            .addHandlerLast(MqttEncoder.INSTANCE);
                    receiveContext.apply(MqttChannel.init(connection));
                });
    }


}