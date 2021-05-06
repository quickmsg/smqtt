package io.github.quickmsg.common.channel;

import io.netty.handler.codec.mqtt.MqttMessage;
import reactor.core.publisher.Mono;

/**
 * @author luxurong
 */
public class MockMqttChannel extends MqttChannel {

    private MockMqttChannel() {

    }

    public final static MockMqttChannel DEFAULT_MOCK_CHANNEL = new MockMqttChannel();


    @Override
    public Mono<Void> write(MqttMessage mqttMessage, boolean retry) {
        return Mono.empty();
    }


    @Override
    public Boolean getIsMock() {
        return true;
    }


}
