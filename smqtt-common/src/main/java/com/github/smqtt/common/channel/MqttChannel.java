package com.github.smqtt.common.channel;

import com.github.smqtt.common.enums.ChannelStatus;
import com.github.smqtt.common.message.MqttEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.Builder;
import lombok.Data;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.netty.Connection;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * @author luxurong
 * @date 2021/3/30 13:43
 * @description
 */
@Builder
@Data
public class MqttChannel {

    private Connection connection;

    private String clientIdentifier;

    private ChannelStatus status;

    private long activeTime;

    private long authTime;

    private boolean sessionPersistent;

    private Will will;

    private long keepalive;

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    private MqttMessageSink mqttMessageSink = new MqttMessageSink();

    private Map<Integer, MqttPublishMessage> qos2MsgCache = new ConcurrentHashMap<>();


    public Mono<Void> cacheQos2Msg(int messageId, MqttPublishMessage publishMessage) {
        return Mono.fromRunnable(() -> qos2MsgCache.put(messageId, publishMessage));
    }

    public Optional<MqttPublishMessage> removeQos2Msg(int messageId) {
        return Optional.ofNullable(qos2MsgCache.remove(messageId));
    }

    public int generateMessageId() {
        int value = atomicInteger.incrementAndGet();
        if (value >= Integer.MAX_VALUE) {
            synchronized (this) {
                if (atomicInteger.intValue() >= Integer.MAX_VALUE) {
                    atomicInteger.set(0);
                }
                value = atomicInteger.incrementAndGet();
            }
        }
        return value;
    }


    @Data
    @Builder
    public static class Will {

        private boolean isRetain;

        private String willTopic;

        private MqttQoS mqttQoS;

        private byte[] willMessage;

    }


    /**
     * 写入消息
     *
     * @param mqttMessage 消息体
     * @return boolean状态
     */
    public Mono<Void> write(MqttMessage mqttMessage, boolean retry) {
        return MqttMessageSink.MQTT_SINK.sendMessage(mqttMessage, this, retry);
    }


    /**
     * 取消重发
     *
     * @param messageId 消息Id
     * @return boolean状态
     */
    public Mono<Void> cancelRetry(Integer messageId) {
        return Mono.fromRunnable(() -> MqttMessageSink.MQTT_SINK.removeReply(messageId));
    }


    /**
     * 写入消息
     *
     * @param buf 消息体
     * @return boolean状态
     */
    private Mono<Void> write(Mono<ByteBuf> buf) {
        return connection.outbound().send(buf).then();
    }

    /**
     * 发送消息并处理重试消息
     */
    private static class MqttMessageSink {

        private MqttMessageSink() {
        }

        public static MqttMessageSink MQTT_SINK = new MqttMessageSink();

        private Function<MqttMessage, ByteBuf> messageTransfer = msg -> MqttEncoder.doEncode(PooledByteBufAllocator.DEFAULT, msg);

        private Map<Integer, Disposable> replyMqttMessageMap = new ConcurrentHashMap<>();


        public Mono<Void> sendMessage(MqttMessage mqttMessage, MqttChannel mqttChannel, boolean retry) {
            ByteBuf byteBuf = messageTransfer.apply(mqttMessage);
            if (retry) {
                ByteBuf retryByteBuf = byteBuf.duplicate().retain();
                return mqttChannel.write(Mono.just(byteBuf)).then(offerReply(setIsDup(retryByteBuf), mqttChannel));
            } else {
                return mqttChannel.write(Mono.just(byteBuf));
            }
        }


        private ByteBuf setIsDup(ByteBuf byteBuf) {
            byte readByte = byteBuf.getByte(0);
            byteBuf.setByte(0, readByte | 0x08);
            return byteBuf;
        }


        public Mono<Void> offerReply(ByteBuf byteBuf, MqttChannel mqttChannel) {
            return Mono.fromRunnable(() ->
                    replyMqttMessageMap.put(
                            mqttChannel.generateMessageId(),
                            mqttChannel.write(Mono.just(byteBuf.duplicate().retain()))
                                    .delaySubscription(Duration.ofSeconds(10))
                                    .repeat()
                                    .doOnError(error -> byteBuf.release())
                                    .doOnCancel(byteBuf::release)
                                    .subscribe()));

        }

        public void removeReply(Integer messageId) {
            Optional.ofNullable(replyMqttMessageMap.get(messageId))
                    .ifPresent(Disposable::dispose);
            replyMqttMessageMap.remove(messageId);
        }
    }


}
