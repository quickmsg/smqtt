package io.github.quickmsg.common.message;

import io.github.quickmsg.common.channel.MqttChannel;
import io.github.quickmsg.common.utils.MessageUtils;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.handler.codec.mqtt.MqttProperties;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttPublishVariableHeader;
import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.Builder;
import lombok.Data;

/**
 * @author luxurong
 */
@Data
@Builder
public class RetainMessage {

    private int qos;

    private String topic;

    private byte[] body;

    private MqttProperties properties;

    public static RetainMessage of(MqttPublishMessage mqttPublishMessage) {
        MqttPublishVariableHeader publishVariableHeader = mqttPublishMessage.variableHeader();
        return RetainMessage.builder()
                .topic(publishVariableHeader.topicName())
                .qos(mqttPublishMessage.fixedHeader().qosLevel().value())
                .body(MessageUtils.copyByteBuf(mqttPublishMessage.payload()))
                .properties(publishVariableHeader.properties())
                .build();
    }

    public MqttPublishMessage toPublishMessage(MqttChannel mqttChannel) {
        return MqttMessageBuilder.buildPub(
                false,
                MqttQoS.valueOf(this.qos),
                qos > 0 ? mqttChannel.generateMessageId() : 0,
                topic,
                PooledByteBufAllocator.DEFAULT.directBuffer().writeBytes(body),
                properties);
    }

}
