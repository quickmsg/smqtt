package io.github.quickmsg.core.protocol;

import io.github.quickmsg.common.auth.PasswordAuthentication;
import io.github.quickmsg.common.channel.ChannelRegistry;
import io.github.quickmsg.common.channel.MqttChannel;
import io.github.quickmsg.common.context.ReceiveContext;
import io.github.quickmsg.common.enums.ChannelStatus;
import io.github.quickmsg.common.message.MessageRegistry;
import io.github.quickmsg.common.message.MqttMessageBuilder;
import io.github.quickmsg.common.message.RecipientRegistry;
import io.github.quickmsg.common.message.SmqttMessage;
import io.github.quickmsg.common.protocol.Protocol;
import io.github.quickmsg.common.topic.SubscribeTopic;
import io.github.quickmsg.common.topic.TopicRegistry;
import io.github.quickmsg.core.mqtt.MqttReceiveContext;
import io.github.quickmsg.metric.counter.WindowMetric;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.mqtt.*;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
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
public class ConnectProtocol implements Protocol<MqttConnectMessage> {

    private static List<MqttMessageType> MESSAGE_TYPE_LIST = new ArrayList<>();

    private static final int MILLI_SECOND_PERIOD = 1_000;


    static {
        MESSAGE_TYPE_LIST.add(MqttMessageType.CONNECT);
    }

    private static void accept(MqttChannel mqttChannel1) {
        WindowMetric.WINDOW_METRIC_INSTANCE.recordConnect(-1);
    }

    @Override
    public Mono<Void> parseProtocol(SmqttMessage<MqttConnectMessage> smqttMessage, MqttChannel mqttChannel, ContextView contextView) {
        MqttConnectMessage message = smqttMessage.getMessage();
        MqttReceiveContext mqttReceiveContext = (MqttReceiveContext) contextView.get(ReceiveContext.class);
        RecipientRegistry recipientRegistry = mqttReceiveContext.getRecipientRegistry();
        MqttConnectVariableHeader mqttConnectVariableHeader = message.variableHeader();
        MqttConnectPayload mqttConnectPayload = message.payload();
        String clientIdentifier = mqttConnectPayload.clientIdentifier();
        ChannelRegistry channelRegistry = mqttReceiveContext.getChannelRegistry();
        TopicRegistry topicRegistry = mqttReceiveContext.getTopicRegistry();
        PasswordAuthentication passwordAuthentication = mqttReceiveContext.getPasswordAuthentication();
        if (channelRegistry.exists(clientIdentifier)){
            return mqttChannel.write(
                    MqttMessageBuilder.buildConnectAck(MqttConnectReturnCode.CONNECTION_REFUSED_IDENTIFIER_REJECTED),
                    false).then(mqttChannel.close());
        }
        if (MqttVersion.MQTT_3_1_1.protocolLevel() != (byte) mqttConnectVariableHeader.version()) {
            return mqttChannel.write(
                    MqttMessageBuilder.buildConnectAck(MqttConnectReturnCode.CONNECTION_REFUSED_UNACCEPTABLE_PROTOCOL_VERSION),
                    false).then(mqttChannel.close());
        }
        if (passwordAuthentication.auth(mqttConnectPayload.userName(), mqttConnectPayload.passwordInBytes())) {
            /*cancel  defer close not authenticate channel */
            mqttChannel.getCloseDisposable().dispose();
            mqttChannel.setClientIdentifier(mqttConnectPayload.clientIdentifier());
            if (mqttConnectVariableHeader.isWillFlag()) {
                mqttChannel.setWill(MqttChannel.Will.builder()
                        .isRetain(mqttConnectVariableHeader.isWillRetain())
                        .willTopic(mqttConnectPayload.willTopic())
                        .willMessage(mqttConnectPayload.willMessageInBytes())
                        .mqttQoS(MqttQoS.valueOf(mqttConnectVariableHeader.willQos()))
                        .build());
            }
            mqttChannel.setAuthTime(System.currentTimeMillis());
            mqttChannel.setKeepalive(mqttConnectVariableHeader.keepAliveTimeSeconds());
            mqttChannel.setSessionPersistent(!mqttConnectVariableHeader.isCleanSession());
            mqttChannel.setStatus(ChannelStatus.ONLINE);
            mqttChannel.setUsername(mqttConnectPayload.userName());
            /*registry unread event close channel */

            mqttChannel.getConnection()
                    .onReadIdle(mqttConnectVariableHeader.keepAliveTimeSeconds() * MILLI_SECOND_PERIOD << 1,
                            () -> close(mqttChannel, mqttReceiveContext));

            /*registry will message send */
            mqttChannel.registryClose(channel -> Optional.ofNullable(mqttChannel.getWill())
                    .ifPresent(will ->
                            topicRegistry.getSubscribesByTopic(will.getWillTopic(), will.getMqttQoS())
                                    .forEach(subscribeTopic -> {
                                        MqttChannel subscribeChannel = subscribeTopic.getMqttChannel();
                                        subscribeChannel.write(
                                                MqttMessageBuilder
                                                        .buildPub(false,
                                                                subscribeTopic.getQoS(),
                                                                subscribeTopic.getQoS() == MqttQoS.AT_MOST_ONCE
                                                                        ? 0 : subscribeChannel.generateMessageId(),
                                                                will.getWillTopic(),
                                                                Unpooled.wrappedBuffer(will.getWillMessage())
                                                        ), subscribeTopic.getQoS().value() > 0
                                        ).subscribe();
                                    })));

            Optional.ofNullable(channelRegistry.get(clientIdentifier))
                    .ifPresent(sessionChannel -> {
                        doSession(sessionChannel, mqttChannel, channelRegistry, topicRegistry, mqttReceiveContext.getMessageRegistry());
                    });

            // registry close mqtt channel event
            mqttChannel.registryClose(channel -> this.close(mqttChannel, mqttReceiveContext));

            channelRegistry.registry(clientIdentifier, mqttChannel);

            WindowMetric.WINDOW_METRIC_INSTANCE.recordConnect(1);

            mqttChannel.registryClose(ConnectProtocol::accept);

            recipientRegistry.channelStatus(mqttChannel, mqttChannel.getStatus());
            mqttChannel.registryClose(mqttChannel1 -> recipientRegistry.channelStatus(mqttChannel1, ChannelStatus.OFFLINE));

            return mqttChannel.write(MqttMessageBuilder.buildConnectAck(MqttConnectReturnCode.CONNECTION_ACCEPTED), false);
        } else {
            return mqttChannel.write(
                    MqttMessageBuilder.buildConnectAck(MqttConnectReturnCode.CONNECTION_REFUSED_BAD_USER_NAME_OR_PASSWORD),
                    false).then(mqttChannel.close());
        }
    }

    private void close(MqttChannel mqttChannel, MqttReceiveContext mqttReceiveContext) {
        if (mqttChannel.isSessionPersistent()) {
            mqttChannel.setStatus(ChannelStatus.OFFLINE);
            mqttChannel.close().subscribe();
        } else {
            mqttReceiveContext.getTopicRegistry().clear(mqttChannel);
            mqttReceiveContext.getChannelRegistry().close(mqttChannel);
        }

    }

    /**
     * 处理session消息
     *
     * @param sessionChannel  session链接 {@link MqttChannel}
     * @param mqttChannel     新链接      {@link MqttChannel}
     * @param channelRegistry {@link ChannelRegistry}
     * @param topicRegistry   {@link TopicRegistry}
     * @param messageRegistry {@link MessageRegistry}
     */
    private void doSession(MqttChannel sessionChannel,
                           MqttChannel mqttChannel,
                           ChannelRegistry channelRegistry,
                           TopicRegistry topicRegistry,
                           MessageRegistry messageRegistry) {
        Set<SubscribeTopic> topics = sessionChannel.getTopics().stream().peek(subscribeTopic ->
                new SubscribeTopic(
                        subscribeTopic.getTopicFilter(),
                        subscribeTopic.getQoS(),
                        subscribeTopic.getMqttChannel()))
                .collect(Collectors.toSet());
        topicRegistry.clear(sessionChannel);
        topics.forEach(topic -> topicRegistry.registrySubscribesTopic(topics));
        channelRegistry.close(sessionChannel);
        Optional.ofNullable(messageRegistry.getSessionMessage(mqttChannel.getClientIdentifier()))
                .ifPresent(sessionMessages -> {
                    sessionMessages.forEach(sessionMessage -> {
                        mqttChannel
                                .write(sessionMessage.toPublishMessage(mqttChannel),
                                        sessionMessage.getQos() > 0)
                                .subscribeOn(Schedulers.single())
                                .subscribe();
                    });
                });
    }


    @Override
    public List<MqttMessageType> getMqttMessageTypes() {
        return MESSAGE_TYPE_LIST;
    }


}
