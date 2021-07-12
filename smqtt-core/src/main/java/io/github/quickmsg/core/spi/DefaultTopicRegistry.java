package io.github.quickmsg.core.spi;

import io.github.quickmsg.common.channel.MqttChannel;
import io.github.quickmsg.common.message.SubscribeChannelContext;
import io.github.quickmsg.common.topic.TopicRegistry;
import io.github.quickmsg.common.utils.TopicRegexUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

/**
 * @author luxurong
 */
public class DefaultTopicRegistry implements TopicRegistry {

    private Map<String, CopyOnWriteArraySet<MqttChannel>> topicChannels = new ConcurrentHashMap<>();


    private LongAdder longAdder = new LongAdder();


    @Override
    public void registryTopicConnection(String topic, MqttChannel mqttChannel) {
        longAdder.increment();
        CopyOnWriteArraySet<MqttChannel> channels = topicChannels.computeIfAbsent(topic, t -> new CopyOnWriteArraySet<>());
        channels.add(mqttChannel);
        mqttChannel.getTopics().add(topic);
    }


    @Override
    public void clear(MqttChannel mqttChannel) {
        longAdder.decrement();
        Set<String> topics = mqttChannel.getTopics();
        this.clear(topics, mqttChannel);
    }

    @Override
    public void clear(Set<String> topics, MqttChannel mqttChannel) {
        Optional.ofNullable(topics)
                .ifPresent(ts -> {
                    for (String topic : ts) {
                        Optional.ofNullable(topicChannels.get(topic))
                                .ifPresent(set -> set.remove(mqttChannel));
                    }
                });

    }

    @Override
    public Set<MqttChannel> getChannelListByTopic(String topicName) {
        Set<String> matchKey = new HashSet<>();
        for (String topic : topicChannels.keySet()) {
            if (topicName.matches(TopicRegexUtils.regexTopic((topic)))) {
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
    public void registryTopicConnection(List<SubscribeChannelContext> mqttTopicSubscriptions) {
        for (SubscribeChannelContext channelContext : mqttTopicSubscriptions) {
            this.registryTopicConnection(channelContext.getTopic(), channelContext.getMqttChannel());
        }

    }

    @Override
    public Map<String, CopyOnWriteArraySet<MqttChannel>> getAllTopics() {
        return this.topicChannels;
    }

    @Override
    public Integer counts() {
        return (int) longAdder.sum();
    }


}
