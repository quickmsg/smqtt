package io.github.quickmsg.core.protocol;

import io.github.quickmsg.common.channel.MqttChannel;
import io.github.quickmsg.common.context.ReceiveContext;
import io.github.quickmsg.common.message.MqttMessageBuilder;
import io.github.quickmsg.common.protocol.Protocol;
import io.github.quickmsg.common.topic.SubscribeTopic;
import io.github.quickmsg.common.topic.TopicRegistry;
import io.github.quickmsg.common.utils.MessageUtils;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageIdVariableHeader;
import io.netty.handler.codec.mqtt.MqttMessageType;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.context.ContextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author luxurong
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
                return Mono.fromRunnable(() -> {
                    mqttChannel.setWill(null);
                    mqttChannel.getConnection().dispose();
                });
            case PUBREC:
                MqttMessageIdVariableHeader messageIdVariableHeader = (MqttMessageIdVariableHeader) message.variableHeader();
                int messageId = messageIdVariableHeader.messageId();
                return mqttChannel.cancelRetry(MqttMessageType.PUBLISH, messageId)
                        .then(mqttChannel.write(MqttMessageBuilder.buildPublishRel(messageId), true));
            case PUBREL:
                MqttMessageIdVariableHeader relMessageIdVariableHeader = (MqttMessageIdVariableHeader) message.variableHeader();
                int id = relMessageIdVariableHeader.messageId();
                /*
                 * 判断是不是缓存qos2消息
                 *       是： 走消息分发 & 回复 comp消息
                 *       否： 直接回复 comp消息
                 */
                return mqttChannel.removeQos2Msg(id)
                        .map(msg -> {
                            ReceiveContext<?> receiveContext = contextView.get(ReceiveContext.class);
                            TopicRegistry topicRegistry = receiveContext.getTopicRegistry();
                            Set<SubscribeTopic> subscribeTopics = topicRegistry.getSubscribesByTopic(msg.variableHeader().topicName(), msg.fixedHeader().qosLevel());
                            return Mono.when(
                                    subscribeTopics.stream()
                                            .map(subscribeTopic -> subscribeTopic.getMqttChannel()
                                                    .write(MessageUtils.wrapPublishMessage(msg, subscribeTopic.getQoS(),
                                                            subscribeTopic.getMqttChannel().generateMessageId()), true)
                                            ).collect(Collectors.toList()))
                                    .then(mqttChannel.cancelRetry(MqttMessageType.PUBREC, id))
                                    .then(mqttChannel.write(MqttMessageBuilder.buildPublishComp(id), false));
                        }).orElseGet(() -> mqttChannel.write(MqttMessageBuilder.buildPublishComp(id), false));

            case PUBCOMP:
                MqttMessageIdVariableHeader messageIdVariableHeader1 = (MqttMessageIdVariableHeader) message.variableHeader();
                int compId = messageIdVariableHeader1.messageId();
                return mqttChannel.cancelRetry(MqttMessageType.PUBREL, compId);
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
