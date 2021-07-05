package io.github.quickmsg.common.message;

import io.github.quickmsg.common.channel.MqttChannel;
import io.github.quickmsg.common.enums.ChannelStatus;
import io.github.quickmsg.common.spi.DynamicLoader;
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


    /**
     * message
     *
     * @param mqttChannel   {@link MqttChannel}
     * @param channelStatus {@link ChannelStatus}
     */
    void channelStatus(MqttChannel mqttChannel, ChannelStatus channelStatus);

}
