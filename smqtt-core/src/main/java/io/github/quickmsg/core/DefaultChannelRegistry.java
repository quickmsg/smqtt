package io.github.quickmsg.core;

import io.github.quickmsg.common.channel.ChannelRegistry;
import io.github.quickmsg.common.channel.MqttChannel;
import io.github.quickmsg.common.enums.ChannelStatus;
import io.github.quickmsg.common.environment.EnvContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author luxurong
 */
@Slf4j
public class DefaultChannelRegistry implements ChannelRegistry {


    private Map<String, MqttChannel> channelMap = new ConcurrentHashMap<>();

    public DefaultChannelRegistry() {
    }

    @Override
    public void startUp(EnvContext envContext) {

    }

    @Override
    public void close(MqttChannel mqttChannel) {
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

    @Override
    public Collection<MqttChannel> getChannels() {
        return channelMap.values();
    }
}
