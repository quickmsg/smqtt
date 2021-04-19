package com.github.smqtt.core.http;

import com.github.smqtt.common.Receiver;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;

/**
 * @author luxurong
 * @date 2021/4/19 15:07
 * @description
 */
public class HttpReceiver implements Receiver {


    @Override
    public Mono<DisposableServer> bind() {
        return HttpServer
                .create()
                .port(6500)
                .route(HttpRouterAcceptor::new)
                .accessLog(true)
                .wiretap(true)
                .bind()
                .cast(DisposableServer.class);
    }
}
