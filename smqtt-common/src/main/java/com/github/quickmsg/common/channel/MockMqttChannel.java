package com.github.quickmsg.common.channel;

import io.netty.handler.codec.mqtt.MqttMessage;
import lombok.Data;
import reactor.core.publisher.Mono;

/**
 * @author luxurong
 * @date 2021/4/21 14:34
 * @description
 */
@Data
public class MockMqttChannel extends MqttChannel {


    @Override
    public Mono<Void> write(MqttMessage mqttMessage, boolean retry) {
        return Mono.empty();
    }


    @Override
    public Boolean getIsMock() {
        return true;
    }


}
