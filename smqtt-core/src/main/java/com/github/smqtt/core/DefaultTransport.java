package com.github.smqtt.core;

import com.github.smqtt.common.Receiver;
import com.github.smqtt.common.context.ReceiveContext;
import com.github.smqtt.common.transport.Transport;
import com.github.smqtt.core.mqtt.MqttConfiguration;
import com.github.smqtt.core.mqtt.MqttReceiveContext;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableChannel;
import reactor.netty.DisposableServer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author luxurong
 * @date 2021/3/30 13:53
 * @description
 */
public class DefaultTransport implements Transport<MqttConfiguration> {


    private Receiver receiver;

    private MqttConfiguration configuration;


    private static List<DisposableServer> disposableServers = new CopyOnWriteArrayList<>();


    public volatile static ReceiveContext<MqttConfiguration> receiveContext;

    public DefaultTransport(MqttConfiguration configuration, Receiver receiver) {
        this.configuration = configuration;
        this.receiver = receiver;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> disposableServers.forEach(DisposableServer::disposeNow)));
    }


    @Override
    public Mono<Transport> start() {
        return Mono.deferContextual(contextView ->
                receiver.bind())
                .doOnNext(this::bindSever)
                .thenReturn(this)
                .cast(Transport.class)
                .contextWrite(context -> context.put(MqttReceiveContext.class, this.buildReceiveContext(configuration)));
    }


    @Override
    public ReceiveContext<MqttConfiguration> buildReceiveContext(MqttConfiguration mqttConfiguration) {
        synchronized (this) {
            if (DefaultTransport.receiveContext == null) {
                DefaultTransport.receiveContext = new MqttReceiveContext(mqttConfiguration, this);
            }
            return DefaultTransport.receiveContext;
        }
    }


    private void bindSever(DisposableServer disposableServer) {
        DefaultTransport.disposableServers.add(disposableServer);
    }


    @Override
    public void dispose() {
        DefaultTransport.disposableServers.forEach(DisposableServer::disposeNow);
    }

    @Override
    public boolean isDisposed() {
        return DefaultTransport.disposableServers.stream().map(DisposableChannel::isDisposed).findAny().orElse(false);
    }


}
