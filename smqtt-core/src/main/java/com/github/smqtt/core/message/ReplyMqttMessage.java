package com.github.smqtt.core.message;

import com.github.smqtt.common.channel.MqttChannel;
import io.netty.handler.codec.mqtt.MqttMessage;
import lombok.Data;

/**
 * @author luxurong
 * @date 2021/4/2 17:02
 * @description
 */
@Data
public class ReplyMqttMessage {

    private MqttMessage mqttMessage;

    private MqttChannel mqttChannel;

}
