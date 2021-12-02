package io.github.quickmsg.core;

import io.github.quickmsg.common.Receiver;
import io.github.quickmsg.common.context.ContextHolder;
import io.github.quickmsg.common.context.ReceiveContext;
import io.github.quickmsg.common.transport.Transport;
import io.github.quickmsg.core.mqtt.MqttConfiguration;
import io.github.quickmsg.core.mqtt.MqttReceiveContext;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;

import java.util.Optional;

/**
 * @author luxurong
 */
@Slf4j
public class DefaultTransport implements Transport<MqttConfiguration> {


    private Receiver receiver;

    private MqttConfiguration configuration;


    private DisposableServer disposableServer;


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
    @SuppressWarnings("unchecked")
    public ReceiveContext<MqttConfiguration> buildReceiveContext(MqttConfiguration mqttConfiguration) {
        synchronized (this) {
            if (ContextHolder.getReceiveContext() == null) {
                MqttReceiveContext receiveContext = new MqttReceiveContext(mqttConfiguration, this);
                ContextHolder.setReceiveContext(receiveContext);
            }
            return (ReceiveContext<MqttConfiguration>) ContextHolder.getReceiveContext();
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
