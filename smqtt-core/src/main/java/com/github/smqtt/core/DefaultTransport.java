package com.github.smqtt.core;

import com.github.smqtt.common.Receiver;
import com.github.smqtt.common.context.ReceiveContext;
import com.github.smqtt.common.transport.Transport;
import com.github.smqtt.core.mqtt.MqttConfiguration;
import com.github.smqtt.core.mqtt.MqttReceiveContext;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;

import java.util.Optional;

/**
 * @author luxurong
 * @date 2021/3/30 13:53
 * @description
 */
@Slf4j
public class DefaultTransport implements Transport<MqttConfiguration> {


    private Receiver receiver;

    private MqttConfiguration configuration;


    private DisposableServer disposableServer;


    public volatile static ReceiveContext<MqttConfiguration> receiveContext;

    public DefaultTransport(MqttConfiguration configuration, Receiver receiver) {
        this.configuration = configuration;
        this.receiver = receiver;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> Optional.ofNullable(disposableServer).ifPresent(DisposableServer::dispose)));
    }


    @Override
    public Mono<Transport> start() {
        return Mono.deferContextual(contextView ->
                receiver.bind())
                .doOnNext(this::bindSever)
                .thenReturn(this)
                .doOnSuccess(defaultTransport -> log.info("server start success host {} port {}", disposableServer.host(), disposableServer.port()))
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
        this.disposableServer = disposableServer;
    }


    @Override
    public void dispose() {
        this.disposableServer.dispose();
    }

    @Override
    public boolean isDisposed() {
        return this.disposableServer.isDisposed();
    }


}
