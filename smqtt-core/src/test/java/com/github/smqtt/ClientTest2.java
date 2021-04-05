package com.github.smqtt;

import com.github.smqtt.common.message.MqttEncoder;
import com.github.smqtt.common.message.MqttMessageBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttQoS;
import org.junit.Test;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.netty.tcp.TcpClient;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author luxurong
 * @date 2021/3/26 15:03
 * @description
 */
public class ClientTest2 {
    private Function<MqttMessage, ByteBuf> messageTransfer = msg -> MqttEncoder.doEncode(PooledByteBufAllocator.DEFAULT, msg);


    @Test
    public void test() throws InterruptedException {


        MqttPublishMessage mqttPublishMessage =
                MqttMessageBuilder.buildPub(false, MqttQoS.AT_MOST_ONCE, false, 1, "topic", Unpooled.wrappedBuffer("adsad".getBytes()));
        ByteBuf byteBuf = messageTransfer.apply(mqttPublishMessage).retain();
        List<Disposable> disposables = new ArrayList<>();
        Disposable disposable = TcpClient.create()
                .remoteAddress(() -> InetSocketAddress.createUnresolved("127.0.0.1", 8111))
                .wiretap(true)
                .doOnConnected(connection -> {
                    int s = byteBuf.refCnt();
                    connection.outbound().send(Mono.just(byteBuf)).then().subscribe();
                    int s2 = byteBuf.refCnt();
                    byteBuf.retain(Integer.MAX_VALUE >> 2);
                    connection
                            .outbound()
                            .send(Mono.just(byteBuf))
                            .then()
                            .doOnCancel(() -> byteBuf.release(byteBuf.refCnt()))
                            .subscribeOn(Schedulers.single())
                            .repeat()
                            .doOnError(throwable -> {
                                System.out.println(throwable.fillInStackTrace());
                            })
                            .subscribe();
                    Disposable a =
                            connection
                                    .outbound()
                                    .send(Mono.just(byteBuf))
                                    .then()
                                    .delaySubscription(Duration.ofSeconds(1))
                                    .doOnCancel(() -> {
                                        byteBuf.release(byteBuf.refCnt());
                                        System.out.println("quxiaole");
                                    })
                                    .repeat()
                                    .subscribeOn(Schedulers.single())
                                    .subscribe();

                    disposables.add(a);
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
