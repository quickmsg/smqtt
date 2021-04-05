package com.github.smqtt.core.protocol;

import com.github.smqtt.common.auth.BasicAuthentication;
import com.github.smqtt.common.channel.ChannelRegistry;
import com.github.smqtt.common.channel.MqttChannel;
import com.github.smqtt.common.context.ReceiveContext;
import com.github.smqtt.common.enums.ChannelStatus;
import com.github.smqtt.common.message.MqttMessageBuilder;
import com.github.smqtt.common.protocol.Protocol;
import com.github.smqtt.common.topic.TopicRegistry;
import com.github.smqtt.core.mqtt.MqttReceiveContext;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.mqtt.*;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.context.ContextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author luxurong
 * @date 2021/3/29 14:05
 * @description server handler
 */
@Slf4j
public class ConnectProtocol implements Protocol<MqttConnectMessage> {

    private static List<MqttMessageType> MESSAGE_TYPE_LIST = new ArrayList<>();


    static {
        MESSAGE_TYPE_LIST.add(MqttMessageType.CONNECT);
    }

    @Override
    public Mono<Void> parseProtocol(MqttConnectMessage message, MqttChannel mqttChannel, ContextView contextView) {
        log.info("mqttChannel connect {} channel {}", message, mqttChannel.getConnection());
        MqttReceiveContext mqttReceiveContext = (MqttReceiveContext) contextView.get(ReceiveContext.class);
        MqttConnectVariableHeader mqttConnectVariableHeader = message.variableHeader();
        MqttConnectPayload mqttConnectPayload = message.payload();
        String clientIdentifier = mqttConnectPayload.clientIdentifier();
        ChannelRegistry channelRegistry = mqttReceiveContext.getChannelRegistry();
        TopicRegistry topicRegistry = mqttReceiveContext.getTopicRegistry();
        BasicAuthentication basicAuthentication = mqttReceiveContext.getBasicAuthentication();
        if (channelRegistry.exists(clientIdentifier)) {
            return mqttChannel.write(
                    MqttMessageBuilder.buildConnectAck(MqttConnectReturnCode.CONNECTION_REFUSED_IDENTIFIER_REJECTED),
                    false).then(mqttChannel.close());
        }
        if (MqttVersion.MQTT_3_1_1.protocolLevel() != (byte) mqttConnectVariableHeader.version()) {
            return mqttChannel.write(
                    MqttMessageBuilder.buildConnectAck(MqttConnectReturnCode.CONNECTION_REFUSED_UNACCEPTABLE_PROTOCOL_VERSION),
                    false).then(mqttChannel.close());
        }
        if (basicAuthentication.auth(mqttConnectPayload.userName(), mqttConnectPayload.passwordInBytes())) {
            /*cancel  defer close not authenticate channel */
            mqttReceiveContext.getDeferCloseDisposable().dispose();
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
            mqttChannel.setSessionPersistent(mqttConnectVariableHeader.isCleanSession());
            mqttChannel.setStatus(ChannelStatus.ONLINE);
            /*registry unread event close channel */
            mqttChannel.getConnection()
                    .onReadIdle(mqttConnectVariableHeader.keepAliveTimeSeconds() << 1,
                            () -> {
                                channelRegistry.close(mqttChannel);
                                topicRegistry.clear(mqttChannel);
                            });
            /*registry will message send */
            mqttChannel
                    .getConnection()
                    .onDispose(() ->
                            Optional.ofNullable(mqttChannel.getWill())
                                    .ifPresent(will -> {
                                        mqttChannel.write(
                                                MqttMessageBuilder
                                                        .buildPub(false,
                                                                will.getMqttQoS(),
                                                                will.isRetain(),
                                                                1,
                                                                will.getWillTopic(),
                                                                Unpooled.wrappedBuffer(will.getWillMessage())
                                                        ), will.getMqttQoS().value() > 0
                                        ).subscribe();
                                    }));
            return mqttChannel.write(MqttMessageBuilder.buildConnectAck(MqttConnectReturnCode.CONNECTION_ACCEPTED), false);


        } else {
            return mqttChannel.write(
                    MqttMessageBuilder.buildConnectAck(MqttConnectReturnCode.CONNECTION_REFUSED_BAD_USER_NAME_OR_PASSWORD),
                    false).then(mqttChannel.close());
        }
    }

    @Override
    public List<MqttMessageType> getMqttMessageTypes() {
        return MESSAGE_TYPE_LIST;
    }


}
