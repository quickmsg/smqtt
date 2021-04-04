package com.github.smqtt.core;

import com.github.smqtt.common.channel.MqttChannel;
import com.github.smqtt.common.topic.TopicRegistry;

import java.util.List;
import java.util.Optional;

/**
 * @author luxurong
 * @date 2021/4/1 15:15
 * @description
 */
public class DefaultTopicRegistry implements TopicRegistry {


    @Override
    public void registryTopicConnection(String topic, MqttChannel mqttChannel) {

    }

    @Override
    public void clear(MqttChannel mqttChannel) {

    }

    @Override
    public Optional<List<MqttChannel>> getChannelListByTopic(String topicName) {
        return Optional.empty();
    }

}
