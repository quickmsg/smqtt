package io.github.quickmsg.common.message;

import io.github.quickmsg.common.channel.MqttChannel;
import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.Builder;
import lombok.Data;

/**
 * @author luxurong
 */
@Data
@Builder
public class SubscribeChannelContext {

    private MqttQoS mqttQoS;

    private MqttChannel mqttChannel;

    private String topic;

}
