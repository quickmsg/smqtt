package com.github.smqtt.common.channel;

import com.github.smqtt.common.spi.DynamicLoader;

/**
 * @author luxurong
 * @date 2021/3/30 13:35
 * @description
 */
public interface ChannelRegistry {


    ChannelRegistry INSTANCE = DynamicLoader.findFirst(ChannelRegistry.class).orElse(null);


    /**
     * 关闭通道
     *
     * @param mqttChannel 通道关闭
     * @return void
     */
    void close(MqttChannel mqttChannel);

    /**
     * 注册通道
     *
     * @param clientIdentifier 客户端id
     * @param mqttChannel 通道关闭
     * @return void
     */
    void registry(String clientIdentifier, MqttChannel mqttChannel);

    /**
     * 判读通道是否存在
     *
     * @param clientIdentifier 客户端id
     * @return boolean
     */
    boolean exists(String clientIdentifier);


    /**
     * 获取通道
     *
     * @param clientIdentifier 客户端id
     * @return MqttChannel
     */
    MqttChannel get(String clientIdentifier);


    /**
     * 获取通道计数
     *
     * @return MqttChannel
     */
    Integer counts();


}
