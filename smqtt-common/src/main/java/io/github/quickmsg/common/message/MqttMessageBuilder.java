package io.github.quickmsg.common.message;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.mqtt.*;

import java.util.List;
import java.util.Map;

import static io.netty.handler.codec.mqtt.MqttConnectReturnCode.CONNECTION_REFUSED_BAD_USERNAME_OR_PASSWORD;
import static io.netty.handler.codec.mqtt.MqttConnectReturnCode.CONNECTION_REFUSED_CLIENT_IDENTIFIER_NOT_VALID;
import static io.netty.handler.codec.mqtt.MqttConnectReturnCode.CONNECTION_REFUSED_NOT_AUTHORIZED_5;
import static io.netty.handler.codec.mqtt.MqttConnectReturnCode.CONNECTION_REFUSED_SERVER_UNAVAILABLE_5;
import static io.netty.handler.codec.mqtt.MqttConnectReturnCode.CONNECTION_REFUSED_UNSUPPORTED_PROTOCOL_VERSION;


/**
 * @author luxurong
 */
public class MqttMessageBuilder {

    private static MqttProperties genMqttProperties(Map<String, String> userPropertiesMap) {
        MqttProperties mqttProperties = null;
        if (userPropertiesMap != null) {
            mqttProperties = new MqttProperties();
            MqttProperties.UserProperties userProperties = new MqttProperties.UserProperties();
            for (Map.Entry<String, String> entry : userPropertiesMap.entrySet()) {
                userProperties.add(entry.getKey(), entry.getValue());
            }
            mqttProperties.add(userProperties);
        }
        return mqttProperties;
    }

    public static MqttPublishMessage buildPub(boolean isDup, MqttQoS qoS, int messageId, String topic, ByteBuf message, MqttProperties properties) {
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.PUBLISH, isDup, qoS, false, 0);
        MqttPublishVariableHeader mqttPublishVariableHeader = new MqttPublishVariableHeader(topic, messageId, properties);
        MqttPublishMessage mqttPublishMessage = new MqttPublishMessage(mqttFixedHeader, mqttPublishVariableHeader, message);
        return mqttPublishMessage;
    }

    public static MqttPublishMessage buildPub(boolean isDup, MqttQoS qoS, int messageId, String topic, ByteBuf message, Map<String, String> userPropertiesMap) {
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.PUBLISH, isDup, qoS, false, 0);
        MqttPublishVariableHeader mqttPublishVariableHeader = new MqttPublishVariableHeader(topic, messageId, genMqttProperties(userPropertiesMap));
        MqttPublishMessage mqttPublishMessage = new MqttPublishMessage(mqttFixedHeader, mqttPublishVariableHeader, message);
        return mqttPublishMessage;
    }

    public static MqttPublishMessage buildPub(boolean isDup, MqttQoS qoS, int messageId, String topic, ByteBuf message) {
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.PUBLISH, isDup, qoS, false, 0);
        MqttPublishVariableHeader mqttPublishVariableHeader = new MqttPublishVariableHeader(topic, messageId);
        MqttPublishMessage mqttPublishMessage = new MqttPublishMessage(mqttFixedHeader, mqttPublishVariableHeader, message);
        return mqttPublishMessage;
    }


    public static MqttPublishMessage buildPub(boolean isDup, MqttQoS qoS, boolean isRetain, int messageId, String topic, ByteBuf message) {
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.PUBLISH, isDup, qoS, isRetain, 0);
        MqttPublishVariableHeader mqttPublishVariableHeader = new MqttPublishVariableHeader(topic, messageId);
        MqttPublishMessage mqttPublishMessage = new MqttPublishMessage(mqttFixedHeader, mqttPublishVariableHeader, message);
        return mqttPublishMessage;
    }


    public static MqttPubAckMessage buildPublishAck(int messageId) {
        return ackMessage(MqttMessageType.PUBACK, messageId, false);
    }

    public static MqttPubAckMessage buildPublishRec(int messageId) {
        return ackMessage(MqttMessageType.PUBREC, messageId, false);
    }

    public static MqttPubAckMessage buildPublishRel(int messageId) {
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.PUBREL, false, MqttQoS.AT_LEAST_ONCE, false, 0x02);
        MqttMessageIdVariableHeader from = MqttMessageIdVariableHeader.from(messageId);
        return new MqttPubAckMessage(mqttFixedHeader, from);
    }


    public static MqttPubAckMessage buildPublishComp(int messageId) {
        return ackMessage(MqttMessageType.PUBCOMP, messageId, false);

    }

    private static MqttPubAckMessage ackMessage(MqttMessageType mqttMessageType, int messageId, boolean isRetain) {
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(mqttMessageType, false, MqttQoS.AT_MOST_ONCE, isRetain, 0x02);
        MqttMessageIdVariableHeader from = MqttMessageIdVariableHeader.from(messageId);
        return new MqttPubAckMessage(mqttFixedHeader, from);
    }


    public static MqttSubAckMessage buildSubAck(int messageId, List<Integer> qos) {
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.SUBACK, false, MqttQoS.AT_MOST_ONCE, false, 0);
        MqttMessageIdVariableHeader variableHeader = MqttMessageIdVariableHeader.from(messageId);
        MqttSubAckPayload payload = new MqttSubAckPayload(qos);
        return new MqttSubAckMessage(mqttFixedHeader, variableHeader, payload);
    }


    public static MqttUnsubAckMessage buildUnsubAck(int messageId) {
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.UNSUBACK, false, MqttQoS.AT_MOST_ONCE, false, 0x02);
        MqttMessageIdVariableHeader variableHeader = MqttMessageIdVariableHeader.from(messageId);
        return new MqttUnsubAckMessage(mqttFixedHeader, variableHeader);
    }

    public static MqttConnAckMessage buildConnectAck(MqttConnectReturnCode connectReturnCode, byte version) {
        MqttProperties properties = MqttProperties.NO_PROPERTIES;
        if (MqttVersion.MQTT_5.protocolLevel() == version) {
            properties = new MqttProperties();
            // support retain msg
            properties.add(new MqttProperties.IntegerProperty(MqttProperties.MqttPropertyType.RETAIN_AVAILABLE.value(), 1));
            // don't support shared subscription
            properties.add(new MqttProperties.IntegerProperty(MqttProperties.MqttPropertyType.SHARED_SUBSCRIPTION_AVAILABLE.value(), 0));
            // mqtt3.0 error code transform
            switch (connectReturnCode) {
                case CONNECTION_REFUSED_IDENTIFIER_REJECTED:
                    connectReturnCode = CONNECTION_REFUSED_CLIENT_IDENTIFIER_NOT_VALID;
                    break;
                case CONNECTION_REFUSED_UNACCEPTABLE_PROTOCOL_VERSION:
                    connectReturnCode = CONNECTION_REFUSED_UNSUPPORTED_PROTOCOL_VERSION;
                    break;
                case CONNECTION_REFUSED_SERVER_UNAVAILABLE:
                    connectReturnCode = CONNECTION_REFUSED_SERVER_UNAVAILABLE_5;
                    break;
                case CONNECTION_REFUSED_BAD_USER_NAME_OR_PASSWORD:
                    connectReturnCode = CONNECTION_REFUSED_BAD_USERNAME_OR_PASSWORD;
                    break;
                case CONNECTION_REFUSED_NOT_AUTHORIZED:
                    connectReturnCode = CONNECTION_REFUSED_NOT_AUTHORIZED_5;
                    break;

            }
        }
        MqttConnAckVariableHeader mqttConnAckVariableHeader = new MqttConnAckVariableHeader(connectReturnCode, false, properties);
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(
                MqttMessageType.CONNACK, false, MqttQoS.AT_MOST_ONCE, false, 0X02);
        return new MqttConnAckMessage(mqttFixedHeader, mqttConnAckVariableHeader);
    }

    public static MqttSubscribeMessage buildSub(int messageId, List<MqttTopicSubscription> topicSubscriptions) {
        MqttSubscribePayload mqttSubscribePayload = new MqttSubscribePayload(topicSubscriptions);
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.SUBSCRIBE, false, MqttQoS.AT_LEAST_ONCE, false, 0);
        MqttMessageIdVariableHeader mqttMessageIdVariableHeader = MqttMessageIdVariableHeader.from(messageId);
        return new MqttSubscribeMessage(mqttFixedHeader, mqttMessageIdVariableHeader, mqttSubscribePayload);
    }

    public static MqttUnsubscribeMessage buildUnSub(int messageId, List<String> topics) {
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.UNSUBSCRIBE, false, MqttQoS.AT_LEAST_ONCE, false, 0x02);
        MqttMessageIdVariableHeader variableHeader = MqttMessageIdVariableHeader.from(messageId);
        MqttUnsubscribePayload MqttUnsubscribeMessage = new MqttUnsubscribePayload(topics);
        return new MqttUnsubscribeMessage(mqttFixedHeader, variableHeader, MqttUnsubscribeMessage);
    }

    public static MqttConnectMessage buildConnect(String clientId, String willTopic, String willMessage, String username, String password, boolean isUsername, boolean isPassword, boolean isWill, int willQos, int heart) {
        MqttConnectVariableHeader mqttConnectVariableHeader = new MqttConnectVariableHeader(MqttVersion.MQTT_3_1_1.protocolName(), MqttVersion.MQTT_3_1_1.protocolLevel(), isUsername, isPassword, false, willQos, isWill, false, heart);
        MqttConnectPayload mqttConnectPayload = new MqttConnectPayload(clientId, willTopic, isWill ? willMessage.getBytes() : null, username, isPassword ? password.getBytes() : null);
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.CONNECT, false, MqttQoS.AT_MOST_ONCE, false, 10);
        return new MqttConnectMessage(mqttFixedHeader, mqttConnectVariableHeader, mqttConnectPayload);
    }


    public static MqttMessage buildPingMessage() {
        return new MqttMessage(new MqttFixedHeader(MqttMessageType.PINGREQ, false, MqttQoS.AT_MOST_ONCE, false, 0));
    }


    public static MqttMessage buildPongMessage() {
        return new MqttMessage(new MqttFixedHeader(MqttMessageType.PINGRESP, false, MqttQoS.AT_MOST_ONCE, false, 0));

    }
}
