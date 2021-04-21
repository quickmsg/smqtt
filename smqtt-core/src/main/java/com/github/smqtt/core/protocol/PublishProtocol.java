package com.github.smqtt.core.protocol;

import com.github.smqtt.common.channel.MqttChannel;
import com.github.smqtt.common.context.ReceiveContext;
import com.github.smqtt.common.enums.ChannelStatus;
import com.github.smqtt.common.message.MessageRegistry;
import com.github.smqtt.common.message.MqttMessageBuilder;
import com.github.smqtt.common.protocol.Protocol;
import com.github.smqtt.common.topic.TopicRegistry;
import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttPublishVariableHeader;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.context.ContextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author luxurong
 * @date 2021/3/29 14:05
 * @description client && server handler
 */
@Slf4j
public class PublishProtocol implements Protocol<MqttPublishMessage> {

    private static List<MqttMessageType> MESSAGE_TYPE_LIST = new ArrayList<>();

    static {
        MESSAGE_TYPE_LIST.add(MqttMessageType.PUBLISH);
    }

    @Override
    public List<MqttMessageType> getMqttMessageTypes() {
        return MESSAGE_TYPE_LIST;
    }


    @Override
    public Mono<Void> parseProtocol(MqttPublishMessage message, MqttChannel mqttChannel, ContextView contextView) {
        try {
            ReceiveContext<?> receiveContext = contextView.get(ReceiveContext.class);
            TopicRegistry topicRegistry = receiveContext.getTopicRegistry();
            MqttPublishVariableHeader variableHeader = message.variableHeader();
            MessageRegistry messageRegistry = receiveContext.getMessageRegistry();
            Optional<Set<MqttChannel>> channelsOptional = topicRegistry.getChannelListByTopic(variableHeader.topicName());
            // http mock
            if (mqttChannel.getIsMock()) {
                return send(channelsOptional, message, messageRegistry, filterRetainMessage(message, messageRegistry));
            }
            switch (message.fixedHeader().qosLevel()) {
                case AT_MOST_ONCE:
                    return send(channelsOptional, message, messageRegistry, filterRetainMessage(message, messageRegistry));
                case AT_LEAST_ONCE:
                    return send(channelsOptional, message, messageRegistry,
                            mqttChannel.write(MqttMessageBuilder.buildPublishAck(variableHeader.packetId()), false)
                                    .then(filterRetainMessage(message, messageRegistry)));
                case EXACTLY_ONCE:
                    if (!mqttChannel.existQos2Msg(variableHeader.packetId())) {
                        return mqttChannel
                                .cacheQos2Msg(variableHeader.packetId(), generateMqttPublishMessage(message, mqttChannel.generateMessageId()))
                                .then(mqttChannel.write(MqttMessageBuilder.buildPublishRec(variableHeader.packetId()), true));
                    }
                default:
                    return Mono.empty();
            }
        } catch (Exception e) {
            log.error("error ", e);
        }
        return Mono.empty();
    }


    /**
     * 判断是不是保留消息
     *
     * @param message         消息体
     * @param messageRegistry 消息中心
     * @return Mono
     */
    private Mono<Void> filterRetainMessage(MqttPublishMessage message, MessageRegistry messageRegistry) {
        return Mono.fromRunnable(() -> {
            if (message.fixedHeader().isRetain()) {
                messageRegistry.saveRetainMessage(message.variableHeader().topicName(), message);
            }
        });
    }


    /**
     * 通用发送消息
     *
     * @param channelsOptional topic 匹配channels
     * @param message          消息体
     * @param messageRegistry  消息中心
     * @param other            其他操作
     * @return Mono
     */
    private Mono<Void> send(Optional<Set<MqttChannel>> channelsOptional, MqttPublishMessage message, MessageRegistry messageRegistry, Mono<Void> other) {
        return channelsOptional.map(channels -> Mono.when(
                channels.stream()
                        .filter(channel -> filterOfflineSession(channel, messageRegistry, message))
                        .map(channel ->
                                channel.write(generateMqttPublishMessage(message,
                                        channel.generateMessageId()),
                                        message.fixedHeader().qosLevel().value() > 0)
                        )
                        .collect(Collectors.toList()))).orElse(Mono.empty()).then(other);

    }


    /**
     * 过滤离线会话消息
     *
     * @param mqttChannel     topic匹配的channel
     * @param messageRegistry 消息注册中心
     * @param mqttMessage     消息
     * @return boolean
     */
    private boolean filterOfflineSession(MqttChannel mqttChannel, MessageRegistry messageRegistry, MqttPublishMessage mqttMessage) {
        if (mqttChannel.getStatus() == ChannelStatus.ONLINE) {
            return true;
        } else {
            messageRegistry
                    .sendSessionMessages(mqttChannel.getClientIdentifier(),
                            generateMqttPublishMessage(mqttMessage, mqttChannel.generateMessageId()));
            return false;
        }
    }


    /**
     * 生成发布消息
     *
     * @param messageId 消息id
     * @param message   消息
     * @return MqttPublishMessage
     */
    private MqttPublishMessage generateMqttPublishMessage(MqttPublishMessage message, int messageId) {
        MqttPublishVariableHeader mqttPublishVariableHeader = message.variableHeader();
        MqttFixedHeader mqttFixedHeader = message.fixedHeader();
        MqttFixedHeader newFixedHeader = new MqttFixedHeader(mqttFixedHeader.messageType(), false, mqttFixedHeader.qosLevel(), false, mqttFixedHeader.remainingLength());
        MqttPublishVariableHeader newHeader = new MqttPublishVariableHeader(mqttPublishVariableHeader.topicName(), messageId);
        return new MqttPublishMessage(newFixedHeader, newHeader, message.payload());

    }


}
