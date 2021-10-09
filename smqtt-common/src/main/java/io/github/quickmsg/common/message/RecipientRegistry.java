package io.github.quickmsg.common.message;

import io.github.quickmsg.common.channel.MqttChannel;
import io.github.quickmsg.common.enums.ChannelStatus;
import io.github.quickmsg.common.spi.DynamicLoader;

/**
 * @author luxurong
 */
public interface RecipientRegistry {

    RecipientRegistry INSTANCE = DynamicLoader.findFirst(RecipientRegistry.class).orElse(null);


    /**
     * 全局消息处理
     *
     * @param mqttChannel     {@link MqttChannel}
     * @param heapMqttMessage {@link HeapMqttMessage}
     */
    @Deprecated
    void accept(MqttChannel mqttChannel, HeapMqttMessage heapMqttMessage);

    /**
     * message
     *
     * @param mqttChannel   {@link MqttChannel}
     * @param channelStatus {@link ChannelStatus}
     */
    void channelStatus(MqttChannel mqttChannel, ChannelStatus channelStatus);

}
