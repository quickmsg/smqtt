package com.github.smqtt.core;

import com.github.smqtt.common.channel.ChannelRegistry;
import com.github.smqtt.common.channel.MqttChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author luxurong
 * @date 2021/3/30 19:20
 * @description
 */
public class DefaultChannelRegistry implements ChannelRegistry {


    private Map<String, MqttChannel> channelMap = new ConcurrentHashMap<>();

    @Override
    public void close(MqttChannel mqttChannel) {
        channelMap.remove(mqttChannel.getClientIdentifier());
    }

    @Override
    public void registry(String clientIdentifier, MqttChannel mqttChannel) {
        channelMap.put(clientIdentifier, mqttChannel);
    }

    @Override
    public boolean exists(String clientIdentifier) {
        return channelMap.containsKey(clientIdentifier);
    }
}
