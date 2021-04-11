package com.github.smqtt.core;

import com.github.smqtt.common.channel.ChannelRegistry;
import com.github.smqtt.common.channel.MqttChannel;
import com.github.smqtt.common.enums.ChannelStatus;
import io.netty.channel.epoll.EpollServerSocketChannelConfig;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Map;
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
        Flux.interval(Duration.ofSeconds(1))
                .subscribe(index -> {
                    log.info("客户端数："+channelMap.size());
                });
    }

    @Override
    public void
    close(MqttChannel mqttChannel) {
        channelMap.remove(mqttChannel.getClientIdentifier());
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
}
