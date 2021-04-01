package com.github.smqtt.common.context;

import com.github.smqtt.common.channel.ChannelRegistry;
import com.github.smqtt.common.channel.MqttChannel;
import com.github.smqtt.common.config.Configuration;
import com.github.smqtt.common.protocol.ProtocolAdaptor;
import com.github.smqtt.common.topic.TopicRegistry;
import io.netty.handler.codec.mqtt.MqttMessage;

import java.util.function.BiConsumer;

/**
 * @author luxurong
 * @date 2021/3/29 20:29
 * @description
 */

public interface ReceiveContext<T extends Configuration> extends BiConsumer<MqttChannel, MqttMessage> {


    /**
     * topic注册中心
     *
     * @param configuration 配置
     * @return TopicRegistry
     */
    TopicRegistry topicRegistry(T configuration);

    /**
     * channel管理中心
     *
     * @param configuration 配置
     * @return ChannelRegistry
     */
    ChannelRegistry channelRegistry(T configuration);


    /**
     * 协议转换器
     *
     * @param configuration 配置
     * @return ProtocolAdaptor
     */
    ProtocolAdaptor protocolAdaptor(T configuration);


    /**
     * 获取配置文件
     *
     * @return T
     */
    T getConfiguration();


}
