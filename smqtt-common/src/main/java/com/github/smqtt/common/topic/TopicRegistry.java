package com.github.smqtt.common.topic;

import com.github.smqtt.common.channel.MqttChannel;
import com.github.smqtt.common.spi.DynamicLoader;

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
}
