package com.github.smqtt;

import org.junit.Test;
import reactor.core.Disposable;
import reactor.netty.tcp.TcpClient;

import java.net.InetSocketAddress;

/**
 * @author luxurong
 * @date 2021/3/26 15:03
 * @description
 */
public class ClientTest {

    @Test
    public void test() throws InterruptedException {

//        System.out.println("5.78.0".compareTo("5.78.0.1")>0);
//        System.out.println("5.78.0".compareTo("5.28.0.1")>0);
//        System.out.println("5.78.0".compareTo("5.98")>0);

        Disposable disposable = TcpClient.create()
                .remoteAddress(() -> InetSocketAddress.createUnresolved("127.0.0.1", 8111))
                .wiretap(true)

                .doOnConnected(connection -> {
                    connection.inbound().receive().asString().subscribe(System.out::println);
                })
                .connect()

                .subscribe();
        Thread.sleep(800000);
        disposable.dispose();
    }
}
