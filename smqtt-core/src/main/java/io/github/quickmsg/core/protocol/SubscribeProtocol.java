package io.github.quickmsg.core.protocol;

import io.github.quickmsg.common.channel.MqttChannel;
import io.github.quickmsg.common.context.ReceiveContext;
import io.github.quickmsg.common.message.MessageRegistry;
import io.github.quickmsg.common.message.MqttMessageBuilder;
import io.github.quickmsg.common.protocol.Protocol;
import io.github.quickmsg.common.topic.SubscribeTopic;
import io.github.quickmsg.common.topic.TopicRegistry;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttSubscribeMessage;
import reactor.core.publisher.Mono;
import reactor.util.context.ContextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author luxurong
 */
public class SubscribeProtocol implements Protocol<MqttSubscribeMessage> {


    private static List<MqttMessageType> MESSAGE_TYPE_LIST = new ArrayList<>();

    @Override
    public Mono<Void> parseProtocol(MqttSubscribeMessage message, MqttChannel mqttChannel, ContextView contextView) {
        return Mono.fromRunnable(() -> {
            ReceiveContext<?> receiveContext = contextView.get(ReceiveContext.class);
            TopicRegistry topicRegistry = receiveContext.getTopicRegistry();
            MessageRegistry messageRegistry = receiveContext.getMessageRegistry();
            Set<SubscribeTopic> mqttTopicSubscriptions =
                    message.payload().topicSubscriptions()
                            .stream()
                            .peek(mqttTopicSubscription -> this.loadRetainMessage(messageRegistry, mqttChannel, mqttTopicSubscription.topicName()))
                            .map(mqttTopicSubscription ->
                                    new SubscribeTopic(mqttTopicSubscription.topicName(), mqttTopicSubscription.qualityOfService(), mqttChannel))
                            .collect(Collectors.toSet());
            topicRegistry.registrySubscribesTopic(mqttTopicSubscriptions);
        }).then(mqttChannel.write(
                MqttMessageBuilder.buildSubAck(
                        message.variableHeader().messageId(),
                        message.payload()
                                .topicSubscriptions()
                                .stream()
                                .map(mqttTopicSubscription ->
                                        mqttTopicSubscription.qualityOfService()
                                                .value())
                                .collect(Collectors.toList())), false));
    }

    private void loadRetainMessage(MessageRegistry messageRegistry, MqttChannel mqttChannel, String topicName) {
        messageRegistry.getRetainMessage(topicName)
                .forEach(retainMessage ->
                        mqttChannel.write(retainMessage.toPublishMessage(mqttChannel), retainMessage.getQos() > 0)
                                .subscribe());
    }

    @Override
    public List<MqttMessageType> getMqttMessageTypes() {
        return MESSAGE_TYPE_LIST;
    }

    static {
        MESSAGE_TYPE_LIST.add(MqttMessageType.SUBSCRIBE);
    }

}
