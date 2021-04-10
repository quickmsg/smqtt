package com.github.smqtt.core.mqtt;

import com.github.smqtt.common.Receiver;
import com.github.smqtt.common.channel.MqttChannel;
import com.github.smqtt.common.enums.ChannelStatus;
import io.netty.channel.ChannelOption;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.handler.codec.mqtt.MqttDecoder;
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
                .flatMap(view -> view.bind().cast(DisposableServer.class));
    }

    private TcpServer newTcpServer(ContextView context) {
        MqttReceiveContext receiveContext = context.get(MqttReceiveContext.class);
        MqttConfiguration mqttConfiguration = receiveContext.getConfiguration();
        return TcpServer.create()
                .port(mqttConfiguration.getPort())
                .doOnBind(mqttConfiguration::loadTcpServerConfig)
                .wiretap(false)
                .childOption(ChannelOption.WRITE_BUFFER_WATER_MARK,new WriteBufferWaterMark(8 * 1024,32 * 1024))
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.SO_REUSEADDR, true)
                .runOn(receiveContext.getLoopResources())
                .doOnConnection(connection -> {
                    connection.addHandler(new MqttDecoder(18000000));
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