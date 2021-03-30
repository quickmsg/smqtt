package com.github.smqtt.core;

import com.github.smqtt.common.Receiver;
import com.github.smqtt.common.channel.ChannelRegistry;
import com.github.smqtt.common.transport.Transport;
import com.github.smqtt.core.mqtt.MqttConfiguration;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableChannel;
import reactor.netty.DisposableServer;

import java.util.List;

/**
 * @author luxurong
 * @date 2021/3/30 13:53
 * @description
 */
public class DefaultTransport implements Transport<MqttConfiguration> {


    private Receiver receiver;

    private MqttConfiguration configuration;

    private List<DisposableServer> disposableServers;


    public DefaultTransport(MqttConfiguration configuration, Receiver receiver) {
        this.configuration = configuration;
        this.receiver = receiver;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            disposableServers.forEach(DisposableServer::disposeNow);
        }));
    }

    @Override
    public Mono<Transport> start(MqttConfiguration mqttConfiguration) {
        return Mono.deferContextual(contextView ->
                receiver.bind())
                .doOnNext(this::init)
                .thenReturn(this)
                .cast(Transport.class)
                .contextWrite(context -> context.put(MqttConfiguration.class, mqttConfiguration));
    }

    private void init(DisposableServer disposableServer) {
        this.disposableServers.add(disposableServer);
    }


    private ChannelRegistry defaultChannelRegistry() {
        return null;
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
