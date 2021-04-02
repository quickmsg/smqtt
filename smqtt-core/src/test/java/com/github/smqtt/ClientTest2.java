package com.github.smqtt;

import com.github.smqtt.common.message.MqttMessageBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttQoS;
import org.junit.Test;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
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
                    ByteBuf byteBuf=Unpooled.wrappedBuffer("ds".getBytes());
                    ByteBuf byteBuf2=byteBuf.duplicate().retain();
                    connection.outbound().send(Mono.just(byteBuf)).then().subscribeOn(Schedulers.single()).subscribe();
                    connection.outbound().send(Mono.just(byteBuf2)).then().subscribeOn(Schedulers.single()).subscribe();
                    connection.outbound().send(Mono.just(byteBuf2)).then().subscribeOn(Schedulers.single()).subscribe();

                })
                .connect()
                .block();
        Thread.sleep(80000);

        disposable.dispose();
    }
}
