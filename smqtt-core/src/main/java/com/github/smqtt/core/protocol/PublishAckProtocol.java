package com.github.smqtt.core.protocol;

import com.github.smqtt.common.channel.MqttChannel;
import com.github.smqtt.common.context.ReceiveContext;
import com.github.smqtt.common.message.MqttMessageBuilder;
import com.github.smqtt.common.protocol.Protocol;
import com.github.smqtt.common.topic.TopicRegistry;
import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessageIdVariableHeader;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttPubAckMessage;
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
 * @description server handler
 */
public class PublishAckProtocol implements Protocol<MqttPubAckMessage> {

    private static List<MqttMessageType> MESSAGE_TYPE_LIST = new ArrayList<>();


    static {
        MESSAGE_TYPE_LIST.add(MqttMessageType.PUBACK);
        MESSAGE_TYPE_LIST.add(MqttMessageType.PUBCOMP);
        MESSAGE_TYPE_LIST.add(MqttMessageType.PUBREC);
        MESSAGE_TYPE_LIST.add(MqttMessageType.PUBREL);
    }


    @Override
    public Mono<Void> parseProtocol(MqttPubAckMessage message, MqttChannel mqttChannel, ContextView contextView) {
        MqttFixedHeader mqttFixedHeader = message.fixedHeader();
        MqttMessageIdVariableHeader idVariableHeader = message.variableHeader();
        int messageId = idVariableHeader.messageId();
        switch (mqttFixedHeader.messageType()) {
            case PUBREC:
                return mqttChannel.cancelRetry(messageId)
                        .then(mqttChannel.write(MqttMessageBuilder.buildPublishRel(messageId), true));

            case PUBREL:
                return mqttChannel.removeQos2Msg(messageId)
                        .map(msg -> {
                            ReceiveContext<?> receiveContext = contextView.get(ReceiveContext.class);
                            TopicRegistry topicRegistry = receiveContext.getTopicRegistry();
                            Optional<Set<MqttChannel>> channelsOptional = topicRegistry.getChannelListByTopic(msg.variableHeader().topicName());
                            return channelsOptional.map(channels -> Mono.when(
                                    channels.stream()
                                            .map(channel -> {
                                                return channel.write(msg, true);
                                            })
                                            .collect(Collectors.toList())
                            )).orElse(Mono.empty());
                        }).orElse(Mono.empty())
                        .then(mqttChannel.cancelRetry(messageId))
                        .then(mqttChannel.write(MqttMessageBuilder.buildPublishComp(messageId), false));
            default:
                return mqttChannel.cancelRetry(messageId);
        }
    }

    @Override
    public List<MqttMessageType> getMqttMessageTypes() {
        return MESSAGE_TYPE_LIST;
    }


}
