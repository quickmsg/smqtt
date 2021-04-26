package com.github.quickmsg.core.protocol;

import com.github.quickmsg.common.channel.MqttChannel;
import com.github.quickmsg.common.context.ReceiveContext;
import com.github.quickmsg.common.message.MessageRegistry;
import com.github.quickmsg.common.message.MqttMessageBuilder;
import com.github.quickmsg.common.message.SubscribeChannelContext;
import com.github.quickmsg.common.protocol.Protocol;
import com.github.quickmsg.common.topic.TopicRegistry;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttSubscribeMessage;
import reactor.core.publisher.Mono;
import reactor.util.context.ContextView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author luxurong
 * @date 2021/3/29 14:05
 * @description server handler
 */
public class SubscribeProtocol implements Protocol<MqttSubscribeMessage> {


    private static List<MqttMessageType> MESSAGE_TYPE_LIST = new ArrayList<>();

    @Override
    public Mono<Void> parseProtocol(MqttSubscribeMessage message, MqttChannel mqttChannel, ContextView contextView) {
        return Mono.fromRunnable(() -> {
            ReceiveContext<?> receiveContext = contextView.get(ReceiveContext.class);
            TopicRegistry topicRegistry = receiveContext.getTopicRegistry();
            MessageRegistry messageRegistry = receiveContext.getMessageRegistry();
            List<SubscribeChannelContext> mqttTopicSubscriptions =
                    message.payload().topicSubscriptions()
                            .stream()
                            .peek(mqttTopicSubscription -> this.loadRetainMessage(messageRegistry, mqttChannel, mqttTopicSubscription.topicName()))
                            .map(mqttTopicSubscription ->
                                    SubscribeChannelContext.builder()
                                            .mqttChannel(mqttChannel)
                                            .mqttQoS(mqttTopicSubscription.qualityOfService())
                                            .topic(mqttTopicSubscription.topicName()).build())
                            .collect(Collectors.toList());
            topicRegistry.registryTopicConnection(mqttTopicSubscriptions);
        }).then(mqttChannel.write(
                MqttMessageBuilder.buildSubAck(
                        message.variableHeader().messageId(),
                        message.payload()
                                .topicSubscriptions()
                                .stream()
                                .map(mqttTopicSubscription ->
                                        mqttTopicSubscription.qualityOfService()
                                                .value())
                                .collect(Collectors.toList())),
                false));
    }

    private void loadRetainMessage(MessageRegistry messageRegistry, MqttChannel mqttChannel, String topicName) {
        messageRegistry.getRetainMessage(topicName, mqttChannel)
                .ifPresent(messages ->
                        Mono.when(messages.stream()
                                .map(message ->
                                        mqttChannel.write(message, message.fixedHeader().qosLevel().value() > 0))
                                .collect(Collectors.toList())
                        ).subscribe()
                );
    }

    @Override
    public List<MqttMessageType> getMqttMessageTypes() {
        return MESSAGE_TYPE_LIST;
    }

    static {
        MESSAGE_TYPE_LIST.add(MqttMessageType.SUBSCRIBE);
    }

}
