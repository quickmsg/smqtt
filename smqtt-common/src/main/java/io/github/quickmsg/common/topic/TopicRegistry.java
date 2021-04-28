package io.github.quickmsg.common.topic;

import io.github.quickmsg.common.message.SubscribeChannelContext;
import io.github.quickmsg.common.channel.MqttChannel;
import io.github.quickmsg.common.spi.DynamicLoader;

import java.util.List;
import java.util.Set;

/**
 * @author luxurong
 * @date 2021/3/30 13:56
 * @description
 */
public interface TopicRegistry {


    TopicRegistry INSTANCE = DynamicLoader.findFirst(TopicRegistry.class).orElse(null);

    /**
     * 绑定主题跟channel关系
     *
     * @param topic       订阅主题
     * @param mqttChannel 通道信息
     * @return 空
     */
    void registryTopicConnection(String topic, MqttChannel mqttChannel);


    /**
     * 清除订阅消息
     *
     * @param mqttChannel 通道信息
     * @return Void
     */
    void clear(MqttChannel mqttChannel);


    /**
     * 清除订阅消息
     *
     * @param topics topics
     * @param mqttChannel 通道信息
     * @return Void
     */
    void clear(Set<String> topics, MqttChannel mqttChannel);

    /**
     * 获取topic的channels
     *
     * @param topicName topic name
     * @return list
     */
    Set<MqttChannel> getChannelListByTopic(String topicName);


    /**
     * 绑定主题跟channel关系
     *
     * @param mqttTopicSubscriptions 通道信息/订阅主题
     * @return 空
     */
    void registryTopicConnection(List<SubscribeChannelContext> mqttTopicSubscriptions);
}
