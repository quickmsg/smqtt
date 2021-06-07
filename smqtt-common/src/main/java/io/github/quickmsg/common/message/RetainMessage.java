package io.github.quickmsg.common.message;

import io.github.quickmsg.common.channel.MqttChannel;
import io.github.quickmsg.common.utils.MessageUtils;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttPublishVariableHeader;
import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @author luxurong
 */
@Data
public class RetainMessage {

    private int qos;

    private String topic;

    private byte[] body;

    public static RetainMessage of( MqttPublishMessage mqttPublishMessage) {
        MqttPublishVariableHeader publishVariableHeader = mqttPublishMessage.variableHeader();
        RetainMessage retainMessage = new RetainMessage();
        retainMessage.setTopic(publishVariableHeader.topicName());
        retainMessage.setQos(mqttPublishMessage.fixedHeader().qosLevel().value());
        retainMessage.setBody(MessageUtils.copyByteBuf(mqttPublishMessage.payload()));
        return retainMessage;
    }

    public MqttPublishMessage toPublishMessage(MqttChannel mqttChannel) {
        return MqttMessageBuilder.buildPub(
                false,
                MqttQoS.valueOf(this.qos),
                qos > 0 ? mqttChannel.generateMessageId() : 0,
                topic,
                PooledByteBufAllocator.DEFAULT.directBuffer().writeBytes(body));
    }

}
