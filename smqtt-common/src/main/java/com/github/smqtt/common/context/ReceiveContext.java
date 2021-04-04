package com.github.smqtt.common.context;

import com.github.smqtt.common.channel.ChannelRegistry;
import com.github.smqtt.common.channel.MqttChannel;
import com.github.smqtt.common.config.Configuration;
import com.github.smqtt.common.message.MessageRegistry;
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
     * @return TopicRegistry
     */
    TopicRegistry getTopicRegistry();

    /**
     * channel管理中心
     *
     * @return ChannelRegistry
     */
    ChannelRegistry getChannelRegistry();


    /**
     * 协议转换器
     *
     * @return ProtocolAdaptor
     */
    ProtocolAdaptor getProtocolAdaptor();


    /**
     * 持久化消息处理
     *
     * @return ProtocolAdaptor
     */
    MessageRegistry getMessageRegistry();


    /**
     * 获取配置文件
     *
     * @return T
     */
    T getConfiguration();


}
