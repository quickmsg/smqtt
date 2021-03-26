package com.github.smqtt;

import io.netty.channel.local.LocalAddress;
import org.junit.Test;
import reactor.core.Disposable;
import reactor.netty.tcp.TcpClient;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * @author luxurong
 * @date 2021/3/26 15:03
 * @description
 */
public class ClientTest {

    @Test
    public void test() throws InterruptedException {


        Disposable disposable= TcpClient.create()
                .remoteAddress(()->InetSocketAddress.createUnresolved("127.0.0.1",8111))
                .wiretap(true)
                .connect()
                .block();
        Thread.sleep(800000);
        disposable.dispose();
    }
}
