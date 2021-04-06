package com.github.smqtt.core;

import com.github.smqtt.common.channel.MqttChannel;
import com.github.smqtt.common.message.SubscribeChannelContext;
import com.github.smqtt.common.topic.TopicRegistry;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author luxurong
 * @date 2021/4/1 15:15
 * @description topic 管理器
 */
public class DefaultTopicRegistry implements TopicRegistry {

    private Map<String, CopyOnWriteArraySet<MqttChannel>> topicChannels = new ConcurrentHashMap<>();


    @Override
    public void registryTopicConnection(String topic, MqttChannel mqttChannel) {
        CopyOnWriteArraySet<MqttChannel> channels = topicChannels.computeIfAbsent(topic, t -> new CopyOnWriteArraySet<>());
        channels.add(mqttChannel);
    }

    @Override
    public void clear(MqttChannel mqttChannel) {
        List<String> topics = mqttChannel.getTopics();
        this.clear(topics, mqttChannel);
    }

    @Override
    public void clear(List<String> topics, MqttChannel mqttChannel) {
        for (String topic : topics) {
            topicChannels.get(topic).remove(mqttChannel);
        }
    }

    @Override
    public Optional<Set<MqttChannel>> getChannelListByTopic(String topicName) {
        return Optional.ofNullable(topicChannels.get(topicName));
    }

    @Override
    //todo not retain subscribe qos
    public void registryTopicConnection(List<SubscribeChannelContext> mqttTopicSubscriptions) {
        for (SubscribeChannelContext channelContext : mqttTopicSubscriptions) {
            this.registryTopicConnection(channelContext.getTopic(), channelContext.getMqttChannel());
        }

    }

}
