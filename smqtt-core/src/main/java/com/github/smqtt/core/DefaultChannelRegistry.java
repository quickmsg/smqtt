package com.github.smqtt.core;

import com.github.smqtt.common.channel.ChannelRegistry;
import com.github.smqtt.common.channel.MqttChannel;
import com.github.smqtt.common.enums.ChannelStatus;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author luxurong
 * @date 2021/3/30 19:20
 * @description
 */
@Slf4j
public class DefaultChannelRegistry implements ChannelRegistry {


    private Map<String, MqttChannel> channelMap = new ConcurrentHashMap<>();

    public DefaultChannelRegistry() {
    }

    @Override
    public void
    close(MqttChannel mqttChannel) {
        Optional.ofNullable(mqttChannel.getClientIdentifier())
                .ifPresent(cliId -> {
                    channelMap.remove(cliId);
                });
        mqttChannel.close().subscribe();
    }

    @Override
    public void registry(String clientIdentifier, MqttChannel mqttChannel) {
        channelMap.put(clientIdentifier, mqttChannel);
    }

    @Override
    public boolean exists(String clientIdentifier) {
        return channelMap.containsKey(clientIdentifier) && channelMap.get(clientIdentifier).getStatus() == ChannelStatus.ONLINE;
    }

    @Override
    public MqttChannel get(String clientIdentifier) {
        return channelMap.get(clientIdentifier);
    }

    @Override
    public Integer counts() {
        return channelMap.size();
    }
}
