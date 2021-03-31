package com.github.smqtt.core.mqtt;

import com.github.smqtt.common.Receiver;
import com.github.smqtt.common.channel.MqttChannel;
import com.github.smqtt.common.enums.ChannelStatus;
import io.netty.channel.ChannelOption;
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
public class MqttReceiver implements Receiver {

    @Override
    public Mono<DisposableServer> bind() {
        return Mono.deferContextual(contextView -> Mono.just(this.newTcpServer(contextView)))
                .flatMap(view ->
                        view.handle((in, out) -> in.receive()
                                .retain()
                                .then())
                                .bind()
                                .cast(DisposableServer.class)
                );
    }

    private TcpServer newTcpServer(ContextView context) {
        MqttReceiveContext receiveContext = context.get(MqttReceiveContext.class);
        MqttConfiguration mqttConfiguration = receiveContext.getConfiguration();
        return TcpServer.create()
                .port(mqttConfiguration.getPort())
                .doOnBind(mqttConfiguration::loadTcpServerConfig)
                .wiretap(true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.SO_REUSEADDR, true)
                .runOn(receiveContext.getLoopResources())
                .doOnConnection(connection ->
                        receiveContext.apply(
                                MqttChannel
                                        .builder()
                                        .activeTime(System.currentTimeMillis())
                                        .connection(connection)
                                        .status(ChannelStatus.INIT)
                                        .build()))
                .doOnChannelInit(
                        (connectionObserver, channel, remoteAddress) -> {
                            channel.pipeline().addLast(MqttEncoder.INSTANCE, new MqttDecoder());
                        });
    }
}