package com.github.smqtt.core;

import com.github.smqtt.common.Receiver;
import com.github.smqtt.common.channel.ChannelRegistry;
import com.github.smqtt.common.context.ReceiveContext;
import com.github.smqtt.common.protocol.ProtocolAdaptor;
import com.github.smqtt.common.spi.DynamicLoader;
import com.github.smqtt.common.transport.Transport;
import com.github.smqtt.core.channel.DefaultChannelRegistry;
import com.github.smqtt.core.mqtt.MqttConfiguration;
import com.github.smqtt.core.mqtt.MqttReceiveContext;
import lombok.Getter;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableChannel;
import reactor.netty.DisposableServer;

import java.util.List;
import java.util.Optional;

/**
 * @author luxurong
 * @date 2021/3/30 13:53
 * @description
 */
public class DefaultTransport implements Transport<MqttConfiguration> {


    private Receiver receiver;

    private MqttConfiguration configuration;

    private List<DisposableServer> disposableServers;


    @Getter
    private ReceiveContext<MqttConfiguration> receiveContext;

    public DefaultTransport(MqttConfiguration configuration, Receiver receiver) {
        this.configuration = configuration;
        this.receiver = receiver;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> disposableServers.forEach(DisposableServer::disposeNow)));
    }

    @Override
    public Mono<Transport> start(MqttConfiguration mqttConfiguration) {
        this.configuration = mqttConfiguration;

        return Mono.deferContextual(contextView ->
                receiver.bind())
                .doOnNext(this::bindSever)
                .thenReturn(this)
                .cast(Transport.class)
                .contextWrite(context -> context.put(MqttReceiveContext.class, this.buildReceiveContext(configuration)));
    }



    @Override
    public ReceiveContext<MqttConfiguration> buildReceiveContext(MqttConfiguration mqttConfiguration) {
        return Optional.ofNullable(receiveContext)
                .orElse((this.receiveContext = new MqttReceiveContext(mqttConfiguration, this)));
    }




    private void bindSever(DisposableServer disposableServer) {
        this.disposableServers.add(disposableServer);
    }


    @Override
    public void dispose() {
        disposableServers.forEach(DisposableServer::disposeNow);
    }

    @Override
    public boolean isDisposed() {
        return disposableServers.stream().map(DisposableChannel::isDisposed).findAny().orElse(false);
    }
}
