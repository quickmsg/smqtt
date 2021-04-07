package com.github.smqtt.common.channel;

import com.github.smqtt.common.enums.ChannelStatus;
import com.github.smqtt.common.message.MqttEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.handler.codec.mqtt.*;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.netty.Connection;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * @author luxurong
 * @date 2021/3/30 13:43
 * @description
 */
@Builder
@Data
@Slf4j
public class MqttChannel {

    private Connection connection;

    private String clientIdentifier;

    private ChannelStatus status;

    private long activeTime;

    private long authTime;

    private boolean sessionPersistent;

    private Will will;

    private long keepalive;

    private List<String> topics;


    private AtomicInteger atomicInteger;

    private MqttMessageSink mqttMessageSink;

    private Map<Integer, MqttPublishMessage> qos2MsgCache;

    private Map<Integer, Disposable> replyMqttMessageMap;


    public MqttChannel initChannel() {
        this.atomicInteger = new AtomicInteger(0);
        this.mqttMessageSink = new MqttMessageSink();
        this.qos2MsgCache = new ConcurrentHashMap<>();
        this.topics = new CopyOnWriteArrayList<>();
        this.replyMqttMessageMap = new ConcurrentHashMap<>();
        return this;
    }


    public Mono<Void> cacheQos2Msg(int messageId, MqttPublishMessage publishMessage) {
        return Mono.fromRunnable(() -> qos2MsgCache.put(messageId, publishMessage));
    }

    public Boolean existQos2Msg(int messageId) {
        return qos2MsgCache.containsKey(messageId);
    }

    public Optional<MqttPublishMessage> removeQos2Msg(int messageId) {
        return Optional.ofNullable(qos2MsgCache.remove(messageId));
    }

    public Mono<Void> close() {
        return Mono.fromRunnable(() -> {
            this.clear();
            this.qos2MsgCache.clear();
            this.topics = null;
            this.connection.dispose();
        });
    }


    public MqttChannel onClose(Disposable disposable) {
        this.connection.onDispose(disposable);
        return this;
    }


    public boolean active() {
        return status == ChannelStatus.ONLINE;
    }

    public int generateMessageId() {
        int value;
        while (qos2MsgCache.containsKey(value = atomicInteger.incrementAndGet())) {
            if (value >= Integer.MAX_VALUE) {
                synchronized (this) {
                    value = atomicInteger.incrementAndGet();
                    if (value >= Integer.MAX_VALUE) {
                        atomicInteger.set(0);
                    } else {
                        break;
                    }
                }
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
        return MqttMessageSink.MQTT_SINK.sendMessage(mqttMessage, this, retry, replyMqttMessageMap);
    }


    /**
     * 取消重发
     *
     * @param messageId 消息Id
     * @return boolean状态
     */
    public Mono<Void> cancelRetry(Integer messageId) {
        return Mono.fromRunnable(() -> this.removeReply(messageId));
    }

    /**
     * remove resend action
     *
     * @param messageId messageId
     * @return void
     */
    private void removeReply(Integer messageId) {
        Optional.ofNullable(replyMqttMessageMap.get(messageId))
                .ifPresent(Disposable::dispose);
        replyMqttMessageMap.remove(messageId);
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


    private void clear() {
        replyMqttMessageMap.values().forEach(Disposable::dispose);
        replyMqttMessageMap.clear();
    }

    /**
     * 发送消息并处理重试消息
     */
    private static class MqttMessageSink {

        private MqttMessageSink() {
        }

        public static MqttMessageSink MQTT_SINK = new MqttMessageSink();

        private Function<MqttMessage, ByteBuf> messageTransfer = msg -> MqttEncoder.doEncode(PooledByteBufAllocator.DEFAULT, msg);


        public Mono<Void> sendMessage(MqttMessage mqttMessage, MqttChannel mqttChannel, boolean retry, Map<Integer, Disposable> replyMqttMessageMap) {
            ByteBuf byteBuf = messageTransfer.apply(mqttMessage);
            if (retry) {
                /*
                Increase the reference count of bytebuf, and the reference count of retrybytebuf is 2
                mqttChannel.write() method releases a reference count.
                 */
                return mqttChannel.write(Mono.just(byteBuf)).then(offerReply(setIsDup(byteBuf.copy().retain(Integer.MAX_VALUE >> 2)), mqttChannel, getMessageId(mqttMessage), replyMqttMessageMap));
            } else {
                return mqttChannel.write(Mono.just(byteBuf));
            }
        }

        private int getMessageId(MqttMessage mqttMessage) {
            Object object = mqttMessage.variableHeader();
            if (object instanceof MqttPublishVariableHeader) {
                return ((MqttPublishVariableHeader) object).packetId();
            } else if (object instanceof MqttMessageIdVariableHeader) {
                return ((MqttMessageIdVariableHeader) object).messageId();
            } else {
                return -1; // client send connect key
            }
        }


        /**
         * Set resend flag
         *
         * @param byteBuf mqttMessage
         * @return ByteBuf
         */
        private ByteBuf setIsDup(ByteBuf byteBuf) {
            byte readByte = byteBuf.getByte(0);
            byteBuf.setByte(0, readByte | 0x08);
            return byteBuf;
        }


        /**
         * Set resend action
         *
         * @param byteBuf             mqttMessage
         * @param mqttChannel         connection
         * @param messageId           messageId
         * @param replyMqttMessageMap
         * @return Mono
         */
        public Mono<Void> offerReply(ByteBuf byteBuf, final MqttChannel mqttChannel, final int messageId, Map<Integer, Disposable> replyMqttMessageMap) {
            return Mono.fromRunnable(() ->
                    replyMqttMessageMap.put(messageId,
                            mqttChannel.write(Mono.just(byteBuf))
                                    .delaySubscription(Duration.ofSeconds(5))
                                    .repeat()
                                    .doOnError(error -> releaseByteBufCount(byteBuf))
                                    .doOnCancel(() -> releaseByteBufCount(byteBuf))
                                    .subscribe()));
        }

        private void releaseByteBufCount(ByteBuf byteBuf) {
            if (byteBuf.refCnt() > 0) {
                int count = byteBuf.refCnt();
                byteBuf.release(count);
                log.info("netty success release reply byteBuf {} count {} ", byteBuf, count);
            }
        }


    }


}
