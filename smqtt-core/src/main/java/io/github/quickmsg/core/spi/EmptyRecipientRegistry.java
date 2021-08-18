package io.github.quickmsg.core.spi;

import io.github.quickmsg.common.channel.MqttChannel;
import io.github.quickmsg.common.enums.ChannelStatus;
import io.github.quickmsg.common.message.RecipientRegistry;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * @author luxurong
 */
@Slf4j
public class EmptyRecipientRegistry implements RecipientRegistry {


    @Override
    public void accept(MqttChannel mqttChannel, MqttPublishMessage publishMessage) {
        log.info("RecipientRegistry message channel {} message {}",mqttChannel,publishMessage);
    }

    @Override
    public void channelStatus(MqttChannel mqttChannel, ChannelStatus channelStatus) {
        log.info("RecipientRegistry channelStatus channel {} status {}",mqttChannel,channelStatus);

    }
}
