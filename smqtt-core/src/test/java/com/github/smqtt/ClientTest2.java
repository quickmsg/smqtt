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
        Disposable disposable = TcpClient.create()
                .remoteAddress(() -> InetSocketAddress.createUnresolved("127.0.0.1", 8111))
                .wiretap(true)
                .doOnConnected(connection -> {
                    ByteBuf byteBuf1 =byteBuf.duplicate().retain(2);
                    connection.outbound().send(Mono.just(byteBuf)).then().subscribe();
                    Byte readByte =byteBuf1.getByte(0);
                    System.out.println(readByte);
                    byteBuf1.setByte(0, readByte | 0x08);//更新索引为0的字节
                    System.out.println(byteBuf.getByte(0));
                    System.out.println(byteBuf1.getByte(0));
                    connection.outbound().send(Mono.just(byteBuf1 )).then().subscribe();


                })
                .connect()
                .block();
        Thread.sleep(80000);

        disposable.dispose();
    }
}
