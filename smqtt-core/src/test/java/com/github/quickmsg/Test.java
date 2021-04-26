package com.github.quickmsg;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.concurrent.CountDownLatch;

/**
 * @author luxurong
 * @date 2021/4/13 14:21
 * @description
 */
public class Test {

    public static void main(String[] args) throws InterruptedException {
        ByteBuf byteBuf  = Unpooled.wrappedBuffer("s".getBytes());
        byteBuf.retain();
        System.out.println(byteBuf.refCnt());
        ByteBuf byteBuf2 = byteBuf.copy();
        System.out.println(byteBuf2.refCnt());

        Mono mono=   Mono.when(Collections.EMPTY_LIST);
        mono.then(Mono.just(1)).subscribe(System.out::println);

        CountDownLatch latch = new CountDownLatch(3);
//       DisposableServer disposableServer = HttpServer.create()
//                .handle((req, resp) ->
//                        resp.sendWebsocket((in, out) -> {
//                            in.receiveCloseStatus()
//                                    .doOnNext(o -> {
//                                        latch.countDown();
//                                    })
//                                    .subscribe();
//
//                            return out.sendString(Flux.interval(Duration.ofMillis(10))
//                                    .map(l -> l + ""));
//                        }))
//                .bindNow();
//
//        HttpClient.create()
//                .port(disposableServer.port())
//                .websocket()
//                .uri("/")
//                .handle((in, out) -> {
//                    in.receiveCloseStatus()
//                            .doOnNext(o -> {
//                                latch.countDown();
//                            })
//                            .subscribe();
//
//                    in.receive()
//                            .take(1)
//                            .subscribe();
//
//                    return Mono.never();
//                })
//                .subscribe();
        latch.await();
    }



}
