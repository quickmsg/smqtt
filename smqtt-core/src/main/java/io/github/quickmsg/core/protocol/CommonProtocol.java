package io.github.quickmsg.core.protocol;

import io.github.quickmsg.common.ack.Ack;
import io.github.quickmsg.common.channel.MqttChannel;
import io.github.quickmsg.common.context.ReceiveContext;
import io.github.quickmsg.common.enums.ChannelStatus;
import io.github.quickmsg.common.message.MessageRegistry;
import io.github.quickmsg.common.message.MqttMessageBuilder;
import io.github.quickmsg.common.message.SessionMessage;
import io.github.quickmsg.common.message.SmqttMessage;
import io.github.quickmsg.common.metric.CounterType;
import io.github.quickmsg.common.metric.MetricManagerHolder;
import io.github.quickmsg.common.protocol.Protocol;
import io.github.quickmsg.common.topic.SubscribeTopic;
import io.github.quickmsg.common.topic.TopicRegistry;
import io.github.quickmsg.common.utils.MessageUtils;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageIdVariableHeader;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.netty.Connection;
import reactor.util.context.ContextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    public Mono<Void> parseProtocol(SmqttMessage<MqttMessage> smqttMessage, MqttChannel mqttChannel, ContextView contextView) {
        ReceiveContext<?> receiveContext = contextView.get(ReceiveContext.class);
        MqttMessage message = smqttMessage.getMessage();
        switch (message.fixedHeader().messageType()) {
            case PINGREQ:
                return mqttChannel.write(MqttMessageBuilder.buildPongMessage(), false);
            case DISCONNECT:
                return Mono.fromRunnable(() -> {
                    MetricManagerHolder.metricManager.getMetricRegistry().getMetricCounter(CounterType.DIS_CONNECT_EVENT).increment();
                    mqttChannel.setWill(null);
                    Connection connection;
                    if (!(connection = mqttChannel.getConnection()).isDisposed()) {
                        connection.dispose();
                    }
                });
            case PUBREC:
                MqttMessageIdVariableHeader messageIdVariableHeader = (MqttMessageIdVariableHeader) message.variableHeader();
                int messageId = messageIdVariableHeader.messageId();
                return Mono.fromRunnable(() -> {
                    Optional.ofNullable(receiveContext.getTimeAckManager().getAck(mqttChannel.generateId(MqttMessageType.PUBLISH, messageId)))
                            .ifPresent(Ack::stop);
                }).then(mqttChannel.write(MqttMessageBuilder.buildPublishRel(messageId), true));
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
                            TopicRegistry topicRegistry = receiveContext.getTopicRegistry();
                            MessageRegistry messageRegistry = receiveContext.getMessageRegistry();
                            Set<SubscribeTopic> subscribeTopics = topicRegistry.getSubscribesByTopic(msg.variableHeader().topicName(), msg.fixedHeader().qosLevel());
                            return Mono.when(
                                            subscribeTopics.stream()
                                                    .filter(subscribeTopic -> filterOfflineSession(subscribeTopic.getMqttChannel(), messageRegistry, MessageUtils.wrapPublishMessage(msg, subscribeTopic.getQoS(), subscribeTopic.getMqttChannel().generateMessageId())))
                                                    .map(subscribeTopic -> subscribeTopic.getMqttChannel()
                                                            .write(MessageUtils.wrapPublishMessage(msg, subscribeTopic.getQoS(),
                                                                    subscribeTopic.getMqttChannel().generateMessageId()), subscribeTopic.getQoS().value() > 0)
                                                    ).collect(Collectors.toList()))
                                    .then(Mono.fromRunnable(() -> {
                                        Optional.ofNullable(receiveContext.getTimeAckManager().getAck(mqttChannel.generateId(MqttMessageType.PUBREC, id)))
                                                .ifPresent(Ack::stop);
                                    }))
                                    .then(mqttChannel.write(MqttMessageBuilder.buildPublishComp(id), false));
                        }).orElseGet(() -> mqttChannel.write(MqttMessageBuilder.buildPublishComp(id), false));

            case PUBCOMP:
                MqttMessageIdVariableHeader messageIdVariableHeader1 = (MqttMessageIdVariableHeader) message.variableHeader();
                int compId = messageIdVariableHeader1.messageId();
                return Mono.fromRunnable(() -> {
                    Optional.ofNullable(receiveContext.getTimeAckManager().getAck(mqttChannel.generateId(MqttMessageType.PUBREL, compId)))
                            .ifPresent(Ack::stop);
                });
            case PINGRESP:
            default:
                return Mono.empty();

        }
    }

    @Override
    public List<MqttMessageType> getMqttMessageTypes() {
        return MESSAGE_TYPE_LIST;
    }

    /**
     * 过滤离线会话消息
     *
     * @param mqttChannel     {@link MqttChannel}
     * @param messageRegistry {@link MessageRegistry}
     * @param mqttMessage     {@link MqttPublishMessage}
     * @return boolean
     */
    private boolean filterOfflineSession(MqttChannel mqttChannel, MessageRegistry messageRegistry, MqttPublishMessage mqttMessage) {
        if (mqttChannel.getStatus() == ChannelStatus.ONLINE) {
            return true;
        } else {
            messageRegistry
                    .saveSessionMessage(SessionMessage.of(mqttChannel.getClientIdentifier(), mqttMessage));
            return false;
        }
    }
}
