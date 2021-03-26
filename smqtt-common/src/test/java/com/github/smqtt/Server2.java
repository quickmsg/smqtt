package com.github.smqtt;

import io.netty.channel.ChannelOption;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.Connection;
import reactor.netty.DisposableServer;
import reactor.netty.tcp.TcpClient;
import reactor.netty.tcp.TcpServer;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

/**
 * @author luxurong
 * @date 2021/3/26 16:24
 * @description
 */
public class Server2 {

    @Test
    public void test() throws InterruptedException {
        DisposableServer server =
                TcpServer.create()
                        .port(0)
                        .childOption(ChannelOption.ALLOW_HALF_CLOSURE, true)
                        .wiretap(true)
                        .handle((in, out) -> in.receive()
                                .asString()
                                .doOnNext(s -> {
                                    if (s.endsWith("257\n")) {
                                        out.sendString(Mono.just("END")
                                                .delayElement(Duration.ofMillis(100)))
                                                .then()
                                                .subscribe();
                                    }
                                })
                                .then())
                        .bindNow();

        Connection conn =
                TcpClient.create()
                        .remoteAddress(server::address)
                        .wiretap(true)
                        .connectNow();

        CountDownLatch latch = new CountDownLatch(1);
        conn.inbound()
                .receive()
                .asString()
                .subscribe(s -> {
                    if ("END".equals(s)) {
                        latch.countDown();
                    }
                });

        conn.outbound()
                .sendString(Flux.range(1, 257).map(count -> count + "\n"))
                .then()
                .subscribe(null, null, () -> ((io.netty.channel.socket.SocketChannel) conn.channel()).shutdownOutput());
        latch.await();
    }

}
