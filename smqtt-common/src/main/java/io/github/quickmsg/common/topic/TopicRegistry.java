package io.github.quickmsg.common.topic;

import io.github.quickmsg.common.channel.MqttChannel;
import io.github.quickmsg.common.spi.DynamicLoader;
import io.netty.handler.codec.mqtt.MqttQoS;

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
     * @param topicFilter 订阅主题
     * @param mqttChannel {@link MqttChannel}
     * @param qos         {@link MqttQoS}
     */
    void registrySubscribeTopic(String topicFilter, MqttChannel mqttChannel, MqttQoS qos);


    /**
     * 绑定主题跟channel关系
     *
     * @param subscribeTopic {@link SubscribeTopic}
     */
    void registrySubscribeTopic(SubscribeTopic subscribeTopic);


    /**
     * 清除订阅消息
     *
     * @param mqttChannel {@link MqttChannel}
     */
    void clear(MqttChannel mqttChannel);


    /**
     * registryTopicConnection
     * 取消订阅关系
     *
     * @param subscribeTopic {@link SubscribeTopic}
     */
    void removeSubscribeTopic(SubscribeTopic subscribeTopic);


    /**
     * 获取topic的channels
     *
     * @param topicName topic name
     * @param qos       {@link MqttQoS}
     * @return {@link SubscribeTopic}
     */
    Set<SubscribeTopic> getSubscribesByTopic(String topicName, MqttQoS qos);


    /**
     * 绑定订阅关系
     *
     * @param subscribeTopics {@link SubscribeTopic}
     */
    void registrySubscribesTopic(Set<SubscribeTopic> subscribeTopics);


    /**
     * 获取所有topic信息
     *
     * @return {@link MqttChannel}
     */
    Map<String, Set<MqttChannel>> getAllTopics();


    /**
     * 获取总数
     *
     * @return counts
     */
    Integer counts();

}
