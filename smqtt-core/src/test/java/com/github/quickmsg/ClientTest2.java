package com.github.quickmsg;

import com.github.quickmsg.common.message.MqttMessageBuilder;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttQoS;
import org.junit.Test;
import reactor.core.Disposable;
import reactor.netty.tcp.TcpClient;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @author luxurong
 * @date 2021/3/26 15:03
 * @description
 */
public class ClientTest2 {


    @Test
    public void test() throws InterruptedException {


        MqttPublishMessage mqttPublishMessage =
                MqttMessageBuilder.buildPub(false, MqttQoS.AT_MOST_ONCE, 1, "topic", Unpooled.wrappedBuffer("adsad".getBytes()));
        List<Disposable> disposables = new ArrayList<>();
        Disposable disposable = TcpClient.create()
                .remoteAddress(() -> InetSocketAddress.createUnresolved("127.0.0.1", 8111))
                .wiretap(true)
                .doOnConnected(connection -> {
                    connection.inbound().receive().asString().subscribe(System.out::println);
//                    connection.outbound().send(Mono.just(byteBuf)).then().subscribe();
//                    int s2 = byteBuf.refCnt();
//                    byteBuf.retain(Integer.MAX_VALUE >> 2);
//                    connection
//                            .outbound()
//                            .send(Mono.just(byteBuf))
//                            .then()
//                            .doOnCancel(() -> byteBuf.release(byteBuf.refCnt()))
//                            .subscribeOn(Schedulers.single())
//                            .repeat()
//                            .doOnError(throwable -> {
//                                System.out.println(throwable.fillInStackTrace());
//                            })
//                            .subscribe();
//                    Disposable a =
//                            connection
//                                    .outbound()
//                                    .send(Mono.just(byteBuf))
//                                    .then()
//                                    .delaySubscription(Duration.ofSeconds(1))
//                                    .doOnCancel(() -> {
//                                        byteBuf.release(byteBuf.refCnt());
//                                        System.out.println("quxiaole");
//                                    })
//                                    .repeat()
//                                    .subscribeOn(Schedulers.single())
//                                    .subscribe();
//
//                    disposables.add(a);
                })
                .connect()
                .doOnError(throwable -> {
                    System.out.println(throwable.fillInStackTrace());
                })
                .block();
        Thread.sleep(10000);
        disposables.get(0).dispose();

        Thread.sleep(80000);


    }
}
