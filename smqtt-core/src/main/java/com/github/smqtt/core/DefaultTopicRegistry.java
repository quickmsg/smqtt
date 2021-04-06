package com.github.smqtt.core;

import com.github.smqtt.common.channel.MqttChannel;
import com.github.smqtt.common.message.SubscribeChannelContext;
import com.github.smqtt.common.topic.TopicRegistry;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

/**
 * @author luxurong
 * @date 2021/4/1 15:15
 * @description topic 管理器
 */
public class DefaultTopicRegistry implements TopicRegistry {

    private Map<String, CopyOnWriteArraySet<MqttChannel>> topicChannels = new ConcurrentHashMap<>();


    @Override
    public void registryTopicConnection(String topic, MqttChannel mqttChannel) {
        CopyOnWriteArraySet<MqttChannel> channels = topicChannels.computeIfAbsent(regex(topic), t -> new CopyOnWriteArraySet<>());
        channels.add(mqttChannel);
        mqttChannel.getTopics().add(topic);
    }

    private static String regex(String topic) {
        if (topic.startsWith("$")) {
            topic = "\\" + topic;
        }
        return topic
                .replaceAll("/", "\\\\/")
                .replaceAll("\\+", "[^/]+")
                .replaceAll("#", "(.+)") + "$";
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
        Set<String> matchKey = new HashSet<>();
        for (String topic : topicChannels.keySet()) {
            if (topicName.matches(topic)) {
                matchKey.add(topic);
            }
        }
        if (matchKey.size() > 0) {
            return Optional.of(matchKey.stream().flatMap(key -> topicChannels.get(key).stream()).collect(Collectors.toSet()));
        } else {
            return Optional.empty();
        }


    }

    @Override
    //todo not retain subscribe qos
    public void registryTopicConnection(List<SubscribeChannelContext> mqttTopicSubscriptions) {
        for (SubscribeChannelContext channelContext : mqttTopicSubscriptions) {
            this.registryTopicConnection(channelContext.getTopic(), channelContext.getMqttChannel());
        }

    }


}
