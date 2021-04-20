package com.github.smqtt.core.websocket;

import com.github.smqtt.common.Receiver;
import com.github.smqtt.common.channel.MqttChannel;
import com.github.smqtt.common.enums.ChannelStatus;
import com.github.smqtt.core.mqtt.MqttConfiguration;
import com.github.smqtt.core.mqtt.MqttReceiveContext;
import com.github.smqtt.core.ssl.AbstractSslHandler;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.mqtt.MqttDecoder;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.tcp.TcpServer;
import reactor.util.context.ContextView;

/**
 * @author luxurong
 * @date 2021/3/29 20:08
 * @description
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
                .wiretap(true)
                .childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(mqttConfiguration.getLowWaterMark(), mqttConfiguration.getHighWaterMark()))
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .runOn(receiveContext.getLoopResources())
                .doOnConnection(connection -> {
                    connection.addHandler(new HttpServerCodec())
                            .addHandler(new HttpObjectAggregator(65536))
                            .addHandler(new WebSocketServerProtocolHandler("/", "mqtt, mqttv3.1, mqttv3.1.1"))
                            .addHandler(new WebSocketFrameToByteBufDecoder())
                            .addHandler(new ByteBufToWebSocketFrameEncoder())
                            .addHandler(new MqttDecoder());
                    receiveContext.apply(
                            MqttChannel
                                    .builder()
                                    .activeTime(System.currentTimeMillis())
                                    .connection(connection)
                                    .status(ChannelStatus.INIT)
                                    .build().initChannel());
                });
    }


}