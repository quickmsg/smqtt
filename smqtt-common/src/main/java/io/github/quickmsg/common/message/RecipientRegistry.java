package io.github.quickmsg.common.message;

import io.github.quickmsg.common.spi.DynamicLoader;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttPublishMessage;

/**
 * @author luxurong
 */
public interface RecipientRegistry {

    RecipientRegistry INSTANCE = DynamicLoader.findFirst(RecipientRegistry.class).orElse(null);


    /**
     * message
     *
     * @param publishMessage {@link MqttPublishMessage}
     */
    void accept(MqttPublishMessage publishMessage);

}
