package io.github.quickmsg.core.spi;

import io.github.quickmsg.common.channel.MqttChannel;
import io.github.quickmsg.common.topic.SubscribeTopic;
import io.github.quickmsg.common.topic.TopicRegistry;
import io.github.quickmsg.core.topic.FixedTopicFilter;
import io.github.quickmsg.core.topic.TopicFilter;
import io.github.quickmsg.core.topic.TreeTopicFilter;
import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author luxurong
 */
@Slf4j
public class DefaultTopicRegistry implements TopicRegistry {


    private static final String ONE_SYMBOL = "+";

    private static final String MORE_SYMBOL = "#";


    private TopicFilter fixedTopicFilter;

    private TopicFilter treeTopicFilter;

    public DefaultTopicRegistry() {
        this.fixedTopicFilter = new FixedTopicFilter();
        this.treeTopicFilter = new TreeTopicFilter();
    }


    @Override
    public void registrySubscribeTopic(String topicFilter, MqttChannel mqttChannel, MqttQoS qos) {
        this.registrySubscribeTopic(new SubscribeTopic(topicFilter, qos, mqttChannel));
    }

    @Override
    public void registrySubscribeTopic(SubscribeTopic subscribeTopic) {
        if (subscribeTopic.getTopicFilter().contains(ONE_SYMBOL) || subscribeTopic.getTopicFilter().contains(MORE_SYMBOL)) {
            treeTopicFilter.addSubscribeTopic(subscribeTopic);
        } else {
            fixedTopicFilter.addSubscribeTopic(subscribeTopic);
        }
    }


    @Override
    public void clear(MqttChannel mqttChannel) {
        Set<SubscribeTopic> topics = mqttChannel.getTopics();
        log.info("mqttChannel channel {} clear topics {}",mqttChannel,topics);
        topics.forEach(this::removeSubscribeTopic);
    }


    @Override
    public void removeSubscribeTopic(SubscribeTopic subscribeTopic) {
        if (subscribeTopic.getTopicFilter().contains(ONE_SYMBOL) || subscribeTopic.getTopicFilter().contains(MORE_SYMBOL)) {
            treeTopicFilter.removeSubscribeTopic(subscribeTopic);
        } else {
            fixedTopicFilter.removeSubscribeTopic(subscribeTopic);
        }
    }


    @Override
    public Set<SubscribeTopic> getSubscribesByTopic(String topicName, MqttQoS qos) {
        Set<SubscribeTopic> subscribeTopics = fixedTopicFilter.getSubscribeByTopic(topicName, qos);
        subscribeTopics.addAll(treeTopicFilter.getSubscribeByTopic(topicName, qos));
        return subscribeTopics;
    }

    @Override
    public void registrySubscribesTopic(Set<SubscribeTopic> mqttTopicSubscriptions) {
        mqttTopicSubscriptions.forEach(this::registrySubscribeTopic);
    }


    @Override
    public Map<String, Set<MqttChannel>> getAllTopics() {
        Set<SubscribeTopic> subscribeTopics = fixedTopicFilter.getAllSubscribesTopic();
        subscribeTopics.addAll(treeTopicFilter.getAllSubscribesTopic());
        return subscribeTopics
                .stream()
                .collect(Collectors.groupingBy(
                        SubscribeTopic::getTopicFilter,
                        Collectors.mapping(SubscribeTopic::getMqttChannel, Collectors.toSet())));
    }

    @Override
    public Integer counts() {
        return fixedTopicFilter.count() + treeTopicFilter.count();
    }


}
