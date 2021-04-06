package com.github.smqtt.core.protocol;

import com.github.smqtt.common.channel.MqttChannel;
import com.github.smqtt.common.context.ReceiveContext;
import com.github.smqtt.common.message.MqttMessageBuilder;
import com.github.smqtt.common.protocol.Protocol;
import com.github.smqtt.common.topic.TopicRegistry;
import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttPublishVariableHeader;
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
public class PublishProtocol implements Protocol<MqttPublishMessage> {

    private static List<MqttMessageType> MESSAGE_TYPE_LIST = new ArrayList<>();

    @Override
    public Mono<Void> parseProtocol(MqttPublishMessage message, MqttChannel mqttChannel, ContextView contextView) {
        ReceiveContext<?> receiveContext = contextView.get(ReceiveContext.class);
        TopicRegistry topicRegistry = receiveContext.getTopicRegistry();
        MqttPublishVariableHeader variableHeader = message.variableHeader();
        Optional<Set<MqttChannel>> channelsOptional = topicRegistry.getChannelListByTopic(variableHeader.topicName());
        switch (message.fixedHeader().qosLevel()) {
            case AT_MOST_ONCE:
                return channelsOptional.map(channels -> Mono.when(
                        channels.stream()
                                .map(channel -> {
                                    return channel.write(generateMqttPublishMessage(message, channel.generateMessageId()), false);
                                })
                                .collect(Collectors.toList())
                )).orElse(Mono.empty());
            case AT_LEAST_ONCE:
                return channelsOptional.map(channels -> Mono.when(
                        channels.stream()
                                .map(channel -> channel
                                        .write(generateMqttPublishMessage(message, channel.generateMessageId()), true))
                                .collect(Collectors.toList())
                )).orElse(Mono.empty()).then(mqttChannel.write(MqttMessageBuilder.buildPublishAck(variableHeader.packetId()
                ), false));
            case EXACTLY_ONCE:
                if (!mqttChannel.existQos2Msg(variableHeader.packetId())) {
                    return mqttChannel
                            .cacheQos2Msg(variableHeader.packetId(), generateMqttPublishMessage(message, mqttChannel.generateMessageId()))
                            .then(mqttChannel.write(MqttMessageBuilder.buildPublishRec(variableHeader.packetId()), true));
                }
            default:
                return Mono.empty();
        }
    }

    private MqttPublishMessage generateMqttPublishMessage(MqttPublishMessage message, int messageId) {
        MqttPublishVariableHeader mqttPublishVariableHeader = message.variableHeader();
        MqttFixedHeader mqttFixedHeader = message.fixedHeader();
        MqttPublishVariableHeader newHeader = new MqttPublishVariableHeader(mqttPublishVariableHeader.topicName(), messageId);
        return new MqttPublishMessage(mqttFixedHeader, newHeader, message.payload().duplicate().retain());

    }


    @Override
    public List<MqttMessageType> getMqttMessageTypes() {
        return MESSAGE_TYPE_LIST;
    }

    static {
        MESSAGE_TYPE_LIST.add(MqttMessageType.PUBLISH);
    }


}
