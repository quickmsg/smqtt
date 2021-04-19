package com.github.smqtt;

import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.http.client.HttpClient;
import reactor.netty.http.server.HttpServer;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

/**
 * @author luxurong
 * @date 2021/4/13 14:21
 * @description
 */
public class Test {

    public static void main(String[] args) throws InterruptedException {


        CountDownLatch latch = new CountDownLatch(3);
       DisposableServer disposableServer = HttpServer.create()
                .handle((req, resp) ->
                        resp.sendWebsocket((in, out) -> {
                            in.receiveCloseStatus()
                                    .doOnNext(o -> {
                                        latch.countDown();
                                    })
                                    .subscribe();

                            return out.sendString(Flux.interval(Duration.ofMillis(10))
                                    .map(l -> l + ""));
                        }))
                .bindNow();

        HttpClient.create()
                .port(disposableServer.port())
                .websocket()
                .uri("/")
                .handle((in, out) -> {
                    in.receiveCloseStatus()
                            .doOnNext(o -> {
                                latch.countDown();
                            })
                            .subscribe();

                    in.receive()
                            .take(1)
                            .subscribe();

                    return Mono.never();
                })
                .subscribe();
        latch.await();
    }



}
