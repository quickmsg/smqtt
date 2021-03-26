package com.github.smqtt;

import io.netty.buffer.Unpooled;
import org.junit.Test;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.netty.tcp.TcpClient;

import java.net.InetSocketAddress;

/**
 * @author luxurong
 * @date 2021/3/26 15:03
 * @description
 */
public class ClientTest2 {

    @Test
    public void test() throws InterruptedException {


        Disposable disposable= TcpClient.create()
                .remoteAddress(()->InetSocketAddress.createUnresolved("127.0.0.1",8111))
                .wiretap(true)
                .doOnConnected(connection -> {
                    connection.outbound().send(Mono.just(Unpooled.wrappedBuffer("sdaasda".getBytes()))).then().subscribe();
                })
                .connect()
                .block();
        Thread.sleep(80000);

        disposable.dispose();
    }
}
