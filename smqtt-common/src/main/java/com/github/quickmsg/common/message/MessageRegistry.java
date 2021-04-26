package com.github.quickmsg.common.message;

import com.github.quickmsg.common.channel.MqttChannel;
import com.github.quickmsg.common.spi.DynamicLoader;
import io.netty.handler.codec.mqtt.MqttPublishMessage;

import java.util.List;
import java.util.Optional;

/**
 * @author luxurong
 * @date 2021/4/4 12:47
 * @description
 */
public interface MessageRegistry {

    MessageRegistry INSTANCE = DynamicLoader.findFirst(MessageRegistry.class).orElse(null);


    /**
     * 获取连接下线后的session消息
     *
     * @param clientIdentifier 设备id
     * @return List
     */
    Optional<List<MqttPublishMessage>> getSessionMessages(String clientIdentifier);


    /**
     * 发送连接下线后的session消息
     *
     * @param clientIdentifier 设备id
     * @param messages         消息
     * @return void
     */
    void sendSessionMessages(String clientIdentifier, MqttPublishMessage messages);


    /**
     * 保留Topic保留消息
     *
     * @param topic    设备id
     * @param messages 消息
     * @return void
     */
    void saveRetainMessage(String topic, MqttPublishMessage messages);


    /**
     * 保留Topic保留消息
     *
     * @param
     * @param topic       设备id
     * @param mqttChannel 通道
     * @return List
     */
    Optional<List<MqttPublishMessage>> getRetainMessage(String topic, MqttChannel mqttChannel);


}
