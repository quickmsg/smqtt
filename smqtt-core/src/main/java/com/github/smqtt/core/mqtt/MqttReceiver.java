package com.github.smqtt.core.mqtt;

import com.github.smqtt.common.Receiver;
import com.github.smqtt.common.channel.MqttChannel;
import com.github.smqtt.core.ssl.AbstractSslHandler;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.tcp.TcpServer;
import reactor.util.context.ContextView;

/**
 * @author luxurong
 * @date 2021/3/29 20:08
 * @description
 */
public class MqttReceiver extends AbstractSslHandler implements Receiver {

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
        return server.port(mqttConfiguration.getPort())
                .doOnBind(mqttConfiguration.getTcpServerConfig())
                .wiretap(mqttConfiguration.getWiretap())
                .childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(mqttConfiguration.getLowWaterMark(), mqttConfiguration.getHighWaterMark()))
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .runOn(receiveContext.getLoopResources())
                .doOnConnection(connection -> {
                    connection.addHandler(new MqttDecoder()).addHandler(MqttEncoder.INSTANCE);
                    receiveContext.apply(MqttChannel.init(connection));
                });
    }
}