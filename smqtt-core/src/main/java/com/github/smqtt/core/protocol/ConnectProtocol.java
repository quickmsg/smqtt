package com.github.smqtt.core.protocol;

import com.github.smqtt.common.auth.BasicAuthentication;
import com.github.smqtt.common.channel.MqttChannel;
import com.github.smqtt.common.enums.ChannelStatus;
import com.github.smqtt.common.message.MqttMessageBuilder;
import com.github.smqtt.common.protocol.Protocol;
import com.github.smqtt.core.mqtt.MqttReceiveContext;
import io.netty.handler.codec.mqtt.*;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.context.ContextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author luxurong
 * @date 2021/3/29 14:05
 * @description
 */
@Slf4j
public class ConnectProtocol implements Protocol<MqttConnectMessage> {

    private static List<MqttMessageType> MESSAGE_TYPE_LIST = new ArrayList<>();


    private BasicAuthentication basicAuthentication;

    static {
        MESSAGE_TYPE_LIST.add(MqttMessageType.CONNECT);
    }

    @Override
    public Mono<Void> parseProtocol(MqttConnectMessage message, MqttChannel mqttChannel, ContextView contextView) {
        MqttConnAckMessage mqttConnAckMessage = MqttMessageBuilder.buildConnectAck(MqttConnectReturnCode.CONNECTION_ACCEPTED);
        log.info("mqttChannel connect {} channel {}", message, mqttChannel.getConnection());
        MqttReceiveContext mqttReceiveContext = contextView.get(MqttReceiveContext.class);
        MqttConnectVariableHeader mqttConnectVariableHeader = message.variableHeader();
        MqttConnectPayload mqttConnectPayload = message.payload();
        if (MqttVersion.MQTT_3_1_1.protocolLevel() != (byte) mqttConnectVariableHeader.version()) {
            mqttConnAckMessage = MqttMessageBuilder.buildConnectAck(MqttConnectReturnCode.CONNECTION_REFUSED_UNACCEPTABLE_PROTOCOL_VERSION);
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
                                mqttReceiveContext.getChannelRegistry().close(mqttChannel);
                                mqttReceiveContext.getTopicRegistry().clear(mqttChannel);
                            });
            /*registry will message send */
            //todo 添加遗嘱发送
            mqttChannel.getConnection().onDispose(()->{

            });

        } else {
            mqttConnAckMessage = MqttMessageBuilder.buildConnectAck(MqttConnectReturnCode.CONNECTION_REFUSED_BAD_USER_NAME_OR_PASSWORD);
        }
        return mqttChannel.write(mqttConnAckMessage,false);
    }

    @Override
    public List<MqttMessageType> getMqttMessageTypes() {
        return MESSAGE_TYPE_LIST;
    }


}
