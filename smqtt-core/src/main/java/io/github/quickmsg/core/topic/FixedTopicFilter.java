package io.github.quickmsg.core.topic;

import io.github.quickmsg.common.channel.MqttChannel;
import io.github.quickmsg.common.topic.SubscribeTopic;
import io.netty.handler.codec.mqtt.MqttQoS;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

/**
 * @author luxurong
 */
public class FixedTopicFilter implements TopicFilter {

    private LongAdder subscribeNumber = new LongAdder();

    private Map<String, CopyOnWriteArraySet<SubscribeTopic>> topicChannels = new ConcurrentHashMap<>();


    @Override
    public Set<SubscribeTopic> getSubscribeByTopic(String topic, MqttQoS mqttQoS) {
        CopyOnWriteArraySet<SubscribeTopic> channels = topicChannels.computeIfAbsent(topic, t -> new CopyOnWriteArraySet<>());
        return channels.stream().map(tp -> tp.compareQos(mqttQoS)).collect(Collectors.toSet());
    }

    @Override
    public void addSubscribeTopic(String topicFilter, MqttChannel mqttChannel, MqttQoS mqttQoS) {
        this.addSubscribeTopic(new SubscribeTopic(topicFilter, mqttQoS, mqttChannel));
    }

    @Override
    public void addSubscribeTopic(SubscribeTopic subscribeTopic) {
        CopyOnWriteArraySet<SubscribeTopic> channels = topicChannels.computeIfAbsent(subscribeTopic.getTopicFilter(), t -> new CopyOnWriteArraySet<>());
        if (channels.add(subscribeTopic)) {
            subscribeNumber.add(1);
            subscribeTopic.linkSubscribe();
        }
    }

    @Override
    public void removeSubscribeTopic(SubscribeTopic subscribeTopic) {
        CopyOnWriteArraySet<SubscribeTopic> channels = topicChannels.computeIfAbsent(subscribeTopic.getTopicFilter(), t -> new CopyOnWriteArraySet<>());
        if (channels.remove(subscribeTopic)) {
            subscribeNumber.add(-1);
            subscribeTopic.unLinkSubscribe();
        }
    }

    @Override
    public int count() {
        return (int) subscribeNumber.sum();
    }
}
