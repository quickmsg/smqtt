package io.github.quickmsg.common.message;

import io.github.quickmsg.common.utils.MessageUtils;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttPublishVariableHeader;
import lombok.Data;

/**
 * @author luxurong
 */

@Data
public class SessionMessage {

    private int qos;

    private String topic;

    private byte[] body;

    private String clientIdentifier;

    private boolean retain;

    public static SessionMessage of(String clientIdentifier, MqttPublishMessage mqttPublishMessage) {
        MqttPublishVariableHeader publishVariableHeader = mqttPublishMessage.variableHeader();
        SessionMessage sessionMessage = new SessionMessage();
        sessionMessage.setClientIdentifier(clientIdentifier);
        sessionMessage.setTopic(publishVariableHeader.topicName());
        sessionMessage.setQos(mqttPublishMessage.fixedHeader().qosLevel().value());
        sessionMessage.setRetain(mqttPublishMessage.fixedHeader().isRetain());
        sessionMessage.setBody(MessageUtils.copyByteBuf(mqttPublishMessage.payload()));
        return sessionMessage;
    }

}
