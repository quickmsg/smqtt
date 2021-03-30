package com.github.smqtt.core.tcp;

import com.github.smqtt.common.ReceiveContext;
import com.github.smqtt.common.Receiver;
import io.netty.channel.ChannelOption;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.tcp.TcpServer;
import reactor.netty.transport.ServerTransportConfig;
import reactor.util.context.ContextView;

import java.util.function.Consumer;

/**
 * @author luxurong
 * @date 2021/3/29 20:08
 * @description
 */
public class TcpReceiver implements Receiver {

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
        TcpReceiveContext receiveContext = (TcpReceiveContext) context.get(ReceiveContext.class);
        Consumer<? super ServerTransportConfig> doOnBind = context.get(Consumer.class);
        TcpConfiguration configuration = receiveContext.getConfiguration();
        return TcpServer.create()
                .port(configuration.getPort())
                .doOnBind(doOnBind)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.SO_REUSEADDR, true)
                .runOn(receiveContext.getLoopResources())
                .doOnChannelInit(
                        (connectionObserver, channel, remoteAddress) -> {
                            channel.pipeline().addLast(MqttEncoder.INSTANCE, new MqttDecoder());
                        });
    }
}