package io.github.quickmsg.core.http;

import io.github.quickmsg.common.Receiver;
import io.github.quickmsg.common.context.ReceiveContext;
import io.github.quickmsg.common.transport.Transport;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableChannel;
import reactor.netty.DisposableServer;

import java.util.Optional;

/**
 * @author luxurong
 */
@Getter
@Slf4j
public class HttpTransport implements Transport<HttpConfiguration> {


    private HttpConfiguration configuration;

    private Receiver receiver;

    private DisposableServer disposableServer;

    public HttpTransport(HttpConfiguration configuration, Receiver receiver) {
        this.configuration = configuration;
        this.receiver = receiver;
    }

    @Override
    public Mono<Transport> start() {
        return Mono.deferContextual(contextView ->
                receiver.bind())
                .doOnNext(this::bindSever)
                .thenReturn(this)
                .doOnSuccess(defaultTransport -> log.info("server start success host {} port {}", disposableServer.host(), disposableServer.port()))
                .cast(Transport.class)
                .contextWrite(context -> context.put(HttpConfiguration.class, getConfiguration()));

    }

    @Override
    public ReceiveContext<HttpConfiguration> buildReceiveContext(HttpConfiguration httpConfiguration) {
        throw new UnsupportedOperationException("no support buildReceiveContext ");
    }

    @Override
    public void dispose() {
        Optional.ofNullable(disposableServer)
                .ifPresent(DisposableChannel::dispose);
    }

    private void bindSever(DisposableServer disposableServer) {
        this.disposableServer = disposableServer;
    }

}
