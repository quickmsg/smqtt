package io.github.quickmsg.core.spi;

import io.github.quickmsg.common.message.RecipientRegistry;
import io.netty.handler.codec.mqtt.MqttPublishMessage;

/**
 * @author luxurong
 */
public class EmptyRecipientRegistry implements RecipientRegistry {


    @Override
    public void accept(MqttPublishMessage message) {

    }
}
