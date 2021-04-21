package com.github.smqtt.common.message;

import io.netty.buffer.PooledByteBufAllocator;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.Data;

import java.util.Map;

/**
 * @author luxurong
 * @date 2021/4/19 14:58
 * @description
 */
@Data
public class HttpPublishMessage {

    private String topic;

    private int qos;

    private boolean retain;

    private String message;

    private Map<String, Object> others;


    public MqttPublishMessage getPublishMessage() {
        return MqttMessageBuilder.buildPub(
                false,
                MqttQoS.valueOf(qos),
                retain,
                1,
                topic,
                PooledByteBufAllocator.DEFAULT.buffer().writeBytes(message.getBytes()));
    }


}
