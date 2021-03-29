package com.github.smqtt.core.protocol;

import com.github.smqtt.common.protocol.Protocol;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttPublishMessage;

/**
 * @author luxurong
 * @date 2021/3/29 14:05
 * @description
 */
public class PublishProtocol implements Protocol<MqttPublishMessage> {

    static {
        MESSAGE_TYPE_LIST.add(MqttMessageType.PUBLISH);
        MESSAGE_TYPE_LIST.add(MqttMessageType.PUBCOMP);
        MESSAGE_TYPE_LIST.add(MqttMessageType.PUBREC);
        MESSAGE_TYPE_LIST.add(MqttMessageType.PUBREL);
    }



    @Override
    public Boolean isProtocol(MqttMessage message) {
        return MESSAGE_TYPE_LIST.contains(message.fixedHeader().messageType());
    }


}
