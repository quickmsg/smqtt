package io.github.quickmsg.common.channel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.quickmsg.common.enums.ChannelStatus;
import io.github.quickmsg.common.topic.SubscribeTopic;
import io.github.quickmsg.common.utils.MessageUtils;
import io.netty.handler.codec.mqtt.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.netty.Connection;

import java.lang.reflect.Constructor;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * @author luxurong
 */
@Getter
@Setter
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

    private String username;

    private String address;

    @JsonIgnore
    private Set<SubscribeTopic> topics;

    @JsonIgnore
    private Boolean isMock = false;

    @JsonIgnore
    private transient AtomicInteger atomicInteger;

    @JsonIgnore
    private transient MqttMessageSink mqttMessageSink;

    @JsonIgnore
    private transient Map<Integer, MqttPublishMessage> qos2MsgCache;

    @JsonIgnore
    private Map<MqttMessageType, Map<Integer, Disposable>> replyMqttMessageMap;

    @JsonIgnore
    private Disposable closeDisposable;


    public void disposableClose() {
        if (closeDisposable != null && !closeDisposable.isDisposed()) {
            closeDisposable.dispose();
        }
    }


    public boolean isActive() {
        return !connection.isDisposed();
    }


    public static MqttChannel init(Connection connection) {
        MqttChannel mqttChannel = new MqttChannel();
        mqttChannel.setTopics(new CopyOnWriteArraySet<>());
        mqttChannel.setAtomicInteger(new AtomicInteger(0));
        mqttChannel.setReplyMqttMessageMap(new ConcurrentHashMap<>());
        mqttChannel.setMqttMessageSink(new MqttMessageSink());
        mqttChannel.setQos2MsgCache(new ConcurrentHashMap<>());
        mqttChannel.setActiveTime(System.currentTimeMillis());
        mqttChannel.setConnection(connection);
        mqttChannel.setStatus(ChannelStatus.INIT);
        mqttChannel.setAddress(connection.address().toString());
        return mqttChannel;
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
            this.clearReplyMessage();
            this.qos2MsgCache.clear();
            if (!this.sessionPersistent) {
                this.topics.clear();
            }
            if (!this.connection.isDisposed()) {
                this.connection.dispose();
            }
        });
    }

    public MqttChannel registryDelayTcpClose() {
        // registry tcp close event
        Connection connection = this.getConnection();
        this.setCloseDisposable(Mono.fromRunnable(() -> {
            if (!connection.isDisposed()) {
                connection.dispose();
            }
        }).delaySubscription(Duration.ofSeconds(10)).subscribe());
        return this;
    }

    public void registryClose(Consumer<MqttChannel> consumer) {
        this.connection.onDispose(() -> consumer.accept(this));
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
     * @param retry       是否重试
     * @return 空操作符
     */
    public Mono<Void> write(MqttMessage mqttMessage, boolean retry) {
        // http本地mock
        if (this.getIsMock() && !this.active()) {
            return Mono.empty();
        } else {
            return MqttMessageSink.MQTT_SINK.sendMessage(mqttMessage, this, retry, replyMqttMessageMap);
        }
    }


    /**
     * 取消重发
     *
     * @param type      type
     * @param messageId 消息Id
     * @return 空操作符
     */
    public Mono<Void> cancelRetry(MqttMessageType type, Integer messageId) {
        return Mono.fromRunnable(() -> this.removeReply(type, messageId));
    }

    /**
     * remove resend action
     *
     * @param type      type
     * @param messageId messageId
     */
    private void removeReply(MqttMessageType type, Integer messageId) {
        Optional.ofNullable(replyMqttMessageMap.get(type))
                .map(messageIds -> messageIds.remove(messageId))
                .ifPresent(Disposable::dispose);
    }


    /**
     * 写入消息
     *
     * @param messageMono 消息体
     * @return 空操作符
     */
    private Mono<Void> write(Mono<MqttMessage> messageMono) {
        if (this.connection.channel().isActive() && this.connection.channel().isWritable()) {
            return connection.outbound().sendObject(messageMono).then();
        } else {
            return Mono.empty();
        }
    }


    private void clearReplyMessage() {
        replyMqttMessageMap.values().forEach(maps -> maps.values().forEach(Disposable::dispose));
        replyMqttMessageMap.clear();
    }

    /**
     * 发送消息并处理重试消息
     */
    private static class MqttMessageSink {

        private MqttMessageSink() {
        }

        public static MqttMessageSink MQTT_SINK = new MqttMessageSink();


        public Mono<Void> sendMessage(MqttMessage mqttMessage, MqttChannel mqttChannel, boolean retry, Map<MqttMessageType, Map<Integer, Disposable>> replyMqttMessageMap) {
            if (log.isDebugEnabled()) {
                log.debug("write channel {} message {}", mqttChannel.getConnection(), mqttMessage);
            }
            if (retry) {
                /*
                Increase the reference count of bytebuf, and the reference count of retrybytebuf is 2
                mqttChannel.write() method releases a reference count.
                 */
                return mqttChannel.write(Mono.just(mqttMessage)).then(offerReply(getReplyMqttMessage(mqttMessage), mqttChannel, getMessageId(mqttMessage), replyMqttMessageMap));
            } else {
                return mqttChannel.write(Mono.just(mqttMessage));
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


        private MqttMessage getReplyMqttMessage(MqttMessage mqttMessage) {
            if (mqttMessage instanceof MqttPublishMessage) {
                return ((MqttPublishMessage) mqttMessage).copy().retain(Integer.MAX_VALUE >> 2);
            } else {
                return mqttMessage;
            }

        }


        /**
         * Set resend flag
         *
         * @param mqttMessage {@link MqttMessage}
         * @return 消息体
         */
        private MqttMessage getDupMessage(MqttMessage mqttMessage) {
            MqttFixedHeader oldFixedHeader = mqttMessage.fixedHeader();
            MqttFixedHeader fixedHeader = new MqttFixedHeader(
                    oldFixedHeader.messageType(),
                    true,
                    oldFixedHeader.qosLevel(),
                    oldFixedHeader.isRetain(),
                    oldFixedHeader.remainingLength());
            Object payload = mqttMessage.payload();
            try {
                Constructor<?> constructor = mqttMessage.getClass().getDeclaredConstructors()[0];
                constructor.setAccessible(true);
                if (constructor.getParameterCount() == 2) {
                    return (MqttMessage) constructor.newInstance(fixedHeader, mqttMessage.variableHeader());
                } else {
                    return (MqttMessage) constructor.newInstance(fixedHeader, mqttMessage.variableHeader(), payload);
                }
            } catch (Exception e) {
                return mqttMessage;
            }

        }


        /**
         * Set resend action
         *
         * @param message             {@link MqttMessage}
         * @param mqttChannel         {@link MqttChannel}
         * @param messageId           messageId
         * @param replyMqttMessageMap 重试缓存
         * @return 空操作符
         */
        public Mono<Void> offerReply(MqttMessage message, final MqttChannel mqttChannel, final int messageId, Map<MqttMessageType, Map<Integer, Disposable>> replyMqttMessageMap) {
            return Mono.fromRunnable(() ->
                    replyMqttMessageMap.computeIfAbsent(message.fixedHeader().messageType(), mqttMessageType -> new ConcurrentHashMap<>(8)).put(messageId,
                            mqttChannel.write(Mono.fromCallable(() -> getDupMessage(message)))
                                    .delaySubscription(Duration.ofSeconds(5))
                                    .repeat(10,mqttChannel::isActive)
                                    .doOnError(error -> {
                                        MessageUtils.safeRelease(message);
                                        log.error("offerReply", error);
                                    })
                                    .doOnCancel(() -> MessageUtils.safeRelease(message))
                                    .subscribe()));
        }

    }

    @Override
    public String toString() {
        return "MqttChannel{" +
//                " address='" + this.connection.address().toString() + '\'' +
                ", clientIdentifier='" + clientIdentifier + '\'' +
                ", status=" + status +
                ", keepalive=" + keepalive +
                ", username='" + username + '}';
    }
}
