package com.github.smqtt.core.protocol;

import com.github.smqtt.common.channel.MqttChannel;
import com.github.smqtt.common.context.ReceiveContext;
import com.github.smqtt.common.message.MqttMessageBuilder;
import com.github.smqtt.common.protocol.Protocol;
import com.github.smqtt.common.topic.TopicRegistry;
import com.github.smqtt.common.utils.MessageUtils;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageIdVariableHeader;
import io.netty.handler.codec.mqtt.MqttMessageType;
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
 * @date 2021/4/1 20:08
 * @description client && server handler
 */
@Slf4j
public class CommonProtocol implements Protocol<MqttMessage> {


    private static List<MqttMessageType> MESSAGE_TYPE_LIST = new ArrayList<>();

    static {
        MESSAGE_TYPE_LIST.add(MqttMessageType.PINGRESP);
        MESSAGE_TYPE_LIST.add(MqttMessageType.PINGREQ);
        MESSAGE_TYPE_LIST.add(MqttMessageType.DISCONNECT);
        MESSAGE_TYPE_LIST.add(MqttMessageType.PUBCOMP);
        MESSAGE_TYPE_LIST.add(MqttMessageType.PUBREC);
        MESSAGE_TYPE_LIST.add(MqttMessageType.PUBREL);
    }


    @Override
    public Mono<Void> parseProtocol(MqttMessage message, MqttChannel mqttChannel, ContextView contextView) {
        switch (message.fixedHeader().messageType()) {
            case PINGREQ:
                return mqttChannel.write(MqttMessageBuilder.buildPongMessage(), false);
            case DISCONNECT:
                mqttChannel.setWill(null);
                mqttChannel.getConnection().dispose();
                return Mono.empty();
            case PUBREC:
                MqttMessageIdVariableHeader messageIdVariableHeader = (MqttMessageIdVariableHeader) message.variableHeader();
                int messageId = messageIdVariableHeader.messageId();
                return mqttChannel.cancelRetry(messageId)
                        .then(mqttChannel.write(MqttMessageBuilder.buildPublishRel(messageId), true));
            case PUBREL:
                MqttMessageIdVariableHeader relMessageIdVariableHeader = (MqttMessageIdVariableHeader) message.variableHeader();
                int id = relMessageIdVariableHeader.messageId();
                return mqttChannel.removeQos2Msg(id)
                        .map(msg -> {
                            ReceiveContext<?> receiveContext = contextView.get(ReceiveContext.class);
                            TopicRegistry topicRegistry = receiveContext.getTopicRegistry();
                            Optional<Set<MqttChannel>> channelsOptional = topicRegistry.getChannelListByTopic(msg.variableHeader().topicName());
                            return channelsOptional.map(channels -> Mono.when(
                                    channels.stream()
                                            .map(channel -> {
                                                return channel.write(MessageUtils.wrapPublishMessage(msg,channel.generateMessageId()), true);
                                            })
                                            .collect(Collectors.toList())
                            )).orElse(Mono.empty())
                                    .doOnSuccess(v->MessageUtils.safeRelease(msg));
                        }).orElse(Mono.empty())
                        .then(mqttChannel.cancelRetry(id))
                        .then(mqttChannel.write(MqttMessageBuilder.buildPublishComp(id), false));
            case PUBCOMP:
                MqttMessageIdVariableHeader messageIdVariableHeader1= (MqttMessageIdVariableHeader) message.variableHeader();
                int compId = messageIdVariableHeader1.messageId();
                return mqttChannel.cancelRetry(compId);
            case PINGRESP:
            default:
                return Mono.empty();

        }
    }

    @Override
    public List<MqttMessageType> getMqttMessageTypes() {
        return MESSAGE_TYPE_LIST;
    }
}
