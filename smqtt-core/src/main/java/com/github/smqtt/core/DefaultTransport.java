package com.github.smqtt.core;

import com.github.smqtt.common.Receiver;
import com.github.smqtt.common.channel.ChannelRegistry;
import com.github.smqtt.common.transport.Transport;
import com.github.smqtt.core.mqtt.TcpConfiguration;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;

/**
 * @author luxurong
 * @date 2021/3/30 13:53
 * @description
 */
public class DefaultTransport implements Transport<TcpConfiguration> {


    private Receiver receiver;

    private TcpConfiguration configuration;

    private DisposableServer disposableServer;


    public DefaultTransport(TcpConfiguration configuration, Receiver receiver) {
        this.configuration = configuration;
        this.receiver = receiver;
    }

    @Override
    public Mono<Transport> start(TcpConfiguration tcpConfiguration) {
        return Mono.deferContextual(contextView ->
                receiver.bind())
                .doOnNext(this::init)
                .thenReturn(this)
                .cast(Transport.class)
                .contextWrite(context -> context.put(TcpConfiguration.class, tcpConfiguration));
    }

    private void init(DisposableServer disposableServer) {
        this.disposableServer = disposableServer;
    }


    private ChannelRegistry defaultChannelRegistry() {
        return null;
    }


}
