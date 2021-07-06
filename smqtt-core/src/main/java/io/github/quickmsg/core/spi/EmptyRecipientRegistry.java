package io.github.quickmsg.core.spi;

import io.github.quickmsg.common.channel.MqttChannel;
import io.github.quickmsg.common.enums.ChannelStatus;
import io.github.quickmsg.common.message.RecipientRegistry;
import io.netty.handler.codec.mqtt.MqttPublishMessage;

/**
 * @author luxurong
 */
public class EmptyRecipientRegistry implements RecipientRegistry {


    @Override
    public void accept(MqttChannel mqttChannel, MqttPublishMessage publishMessage) {

    }

    @Override
    public void channelStatus(MqttChannel mqttChannel, ChannelStatus channelStatus) {

    }
}
