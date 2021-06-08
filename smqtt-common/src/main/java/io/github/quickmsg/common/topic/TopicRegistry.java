package io.github.quickmsg.common.topic;

import io.github.quickmsg.common.channel.MqttChannel;
import io.github.quickmsg.common.message.SubscribeChannelContext;
import io.github.quickmsg.common.spi.DynamicLoader;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author luxurong
 */
public interface TopicRegistry {


    TopicRegistry INSTANCE = DynamicLoader.findFirst(TopicRegistry.class).orElse(null);

    /**
     * 绑定主题跟channel关系
     *
     * @param topic       订阅主题
     * @param mqttChannel {@link MqttChannel}
     */
    void registryTopicConnection(String topic, MqttChannel mqttChannel);


    /**
     * 清除订阅消息
     *
     * @param mqttChannel {@link MqttChannel}
     */
    void clear(MqttChannel mqttChannel);


    /**
     * 清除订阅消息
     *
     * @param topics topics
     * @param mqttChannel {@link MqttChannel}
     */
    void clear(Set<String> topics, MqttChannel mqttChannel);

    /**
     * 获取topic的channels
     *
     * @param topicName topic name
     * @return {@link Set<MqttChannel>}
     */
    Set<MqttChannel> getChannelListByTopic(String topicName);


    /**
     * 绑定主题跟channel关系
     *
     * @param mqttTopicSubscriptions {@link SubscribeChannelContext}
     */
    void registryTopicConnection(List<SubscribeChannelContext> mqttTopicSubscriptions);


    /**
     * 获取所有topic信息
     *
     * @return  {@link MqttChannel}
     */
    Map<String, CopyOnWriteArraySet<MqttChannel>> getAllTopics();
}
