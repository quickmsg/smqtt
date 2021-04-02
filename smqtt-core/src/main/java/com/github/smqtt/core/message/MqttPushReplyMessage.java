package com.github.smqtt.core.message;

import com.github.smqtt.common.message.ReplyMessage;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttPublishMessage;

/**
 * @author luxurong
 * @date 2021/4/2 18:33
 * @description
 */
public class MqttPushReplyMessage extends ReplyMessage<MqttPublishMessage> {

    @Override
    public MqttMessage duplicate(MqttPublishMessage mqttMessage) {
        return mqttMessage.duplicate().retain();
    }

    @Override
    public void release(MqttPublishMessage mqttMessage) {
        mqttMessage.release();
    }

    @Override
    public Integer messageId() {
       return this.getMqttMessage().variableHeader().packetId();
    }
}
