package com.github.smqtt.common.message;

import com.github.smqtt.common.channel.MqttChannel;
import io.netty.handler.codec.mqtt.MqttMessage;
import lombok.Data;
import reactor.core.publisher.Mono;

/**
 * @author luxurong
 * @date 2021/4/2 17:02
 * @description
 */
@Data
public abstract class ReplyMessage<T extends MqttMessage> {

    private T mqttMessage;

    private MqttChannel mqttChannel;

    public ReplyMessage(T mqttMessage, MqttChannel mqttChannel) {
        this.mqttMessage = mqttMessage;
        this.mqttChannel = mqttChannel;
    }

    public Mono<Void> send() {
        return this.mqttChannel.write(mqttMessage);
    }

    public Mono<Void> sendRetain() {
        return this.mqttChannel.write(duplicate(this.mqttMessage));
    }


    public void clear() {
        release(this.mqttMessage);
        this.mqttMessage = null;
        this.mqttChannel = null;
    }

    public abstract MqttMessage duplicate(T mqttMessage);

    public abstract void release(T mqttMessage);


    public abstract Integer messageId();


}
