package io.github.quickmsg.common.message;

import io.netty.handler.codec.mqtt.MqttMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author luxurong
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
public class SmqttMessage<T extends MqttMessage> {

    private T message;

    private long timestamp;

    private Boolean isCluster;

}
