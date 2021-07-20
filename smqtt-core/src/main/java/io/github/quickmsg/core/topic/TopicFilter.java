package io.github.quickmsg.core.topic;

import io.github.quickmsg.common.channel.MqttChannel;
import io.github.quickmsg.common.topic.SubscribeTopic;
import io.netty.handler.codec.mqtt.MqttQoS;

import java.util.Set;

/**
 * @author luxurong
 */
public interface TopicFilter {

    /**
     * 获取订阅topic
     *
     * @param topic   topic
     * @param mqttQoS {@link MqttQoS}
     * @return {@link SubscribeTopic}
     */
    Set<SubscribeTopic> getSubscribeByTopic(String topic, MqttQoS mqttQoS);


    /**
     * 保存订阅topic
     *
     * @param topicFilter topicFilter
     * @param mqttQoS     {@link MqttQoS}
     * @param mqttChannel {@link MqttChannel}
     */
    void addSubscribeTopic(String topicFilter, MqttChannel mqttChannel, MqttQoS mqttQoS);


    /**
     * 保存订阅topic
     *
     * @param subscribeTopic {@link SubscribeTopic}
     */
    void addSubscribeTopic(SubscribeTopic subscribeTopic);


    /**
     * 保存订阅topic
     *
     * @param subscribeTopic {@link SubscribeTopic}
     */
    void removeSubscribeTopic(SubscribeTopic subscribeTopic);


    /**
     * 获取订阅总数
     *
     * @return 总数
     */
    int count();


}
