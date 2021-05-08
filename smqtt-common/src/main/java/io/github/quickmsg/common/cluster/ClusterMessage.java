package io.github.quickmsg.common.cluster;

import io.github.quickmsg.common.message.MqttMessageBuilder;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author luxurong
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClusterMessage {

    private String topic;

    private int qos;

    private boolean retain;

    private byte[] message;

    public MqttPublishMessage getMqttMessage() {
        return MqttMessageBuilder
                .buildPub(false,
                        MqttQoS.valueOf(qos),
                        0,
                        topic,
                        PooledByteBufAllocator.DEFAULT.buffer().writeBytes(message));
    }
}
