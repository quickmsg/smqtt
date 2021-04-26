package com.github.quickmsg.common.message;

import com.github.quickmsg.common.channel.MqttChannel;
import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.Builder;
import lombok.Data;

/**
 * @author luxurong
 * @date 2021/4/4 20:10
 * @description
 */
@Data
@Builder
public class SubscribeChannelContext {

    private MqttQoS mqttQoS;

    private MqttChannel mqttChannel;

    private String topic;

}
