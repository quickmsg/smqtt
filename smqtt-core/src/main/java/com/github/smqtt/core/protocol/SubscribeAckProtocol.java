package com.github.smqtt.core.protocol;

import com.github.smqtt.common.protocol.Protocol;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttSubscribeMessage;
import io.netty.handler.codec.mqtt.MqttUnsubscribeMessage;

/**
 * @author luxurong
 * @date 2021/3/29 14:05
 * @description
 */
public class SubscribeAckProtocol implements Protocol<MqttUnsubscribeMessage> {

    static {
        MESSAGE_TYPE_LIST.add(MqttMessageType.SUBACK);
    }


    @Override
    public Boolean isProtocol(MqttMessage message) {
        return MESSAGE_TYPE_LIST.contains(message.fixedHeader().messageType());
    }


}
