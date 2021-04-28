package com.github.quickmsg.core;

import com.github.quickmsg.common.channel.MqttChannel;
import com.github.quickmsg.common.message.SubscribeChannelContext;
import com.github.quickmsg.common.topic.TopicRegistry;
import com.github.quickmsg.common.utils.TopicRegexUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

/**
 * @author luxurong
 */
public class DefaultTopicRegistry implements TopicRegistry {

    private Map<String, CopyOnWriteArraySet<MqttChannel>> topicChannels = new ConcurrentHashMap<>();


    @Override
    public void registryTopicConnection(String topic, MqttChannel mqttChannel) {
        CopyOnWriteArraySet<MqttChannel> channels = topicChannels.computeIfAbsent(TopicRegexUtils.regexTopic(topic), t -> new CopyOnWriteArraySet<>());
        channels.add(mqttChannel);
        mqttChannel.getTopics().add(topic);
    }


    @Override
    public void clear(MqttChannel mqttChannel) {
        Set<String> topics = mqttChannel.getTopics();
        this.clear(topics, mqttChannel);
    }

    @Override
    public void clear(Set<String> topics, MqttChannel mqttChannel) {
        for (String topic : topics) {
            topicChannels.get(TopicRegexUtils.regexTopic(topic)).remove(mqttChannel);
        }
    }

    @Override
    public Set<MqttChannel> getChannelListByTopic(String topicName) {
        Set<String> matchKey = new HashSet<>();
        for (String topic : topicChannels.keySet()) {
            if (topicName.matches(topic)) {
                matchKey.add(topic);
            }
        }
        if (matchKey.size() > 0) {
            return matchKey.stream().flatMap(key -> topicChannels.get(key).stream()).collect(Collectors.toSet());
        } else {
            return Collections.emptySet();
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
