package com.github.smqtt.common.protocol;

import com.github.smqtt.common.channel.MqttChannel;
import com.github.smqtt.common.config.Configuration;
import com.github.smqtt.common.context.ReceiveContext;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageType;

import java.util.List;

/**
 * @author luxurong
 * @date 2021/3/26 13:55
 * @description 协议转换接口
 */
public interface Protocol<T extends MqttMessage,C extends Configuration> {


    /**
     * 判断协议是不是实现类型
     *
     * @param message 消息实体
     * @return Boolean
     */
    Boolean isProtocol(MqttMessage message);

    /**
     * 解析协议
     *
     * @param message 消息类型
     * @param mqttChannel
     * @param receiveContext
     * @return T
     */
    default T doParseProtocol(MqttMessage message, MqttChannel mqttChannel, ReceiveContext<C> receiveContext) {
        return (T) message;
    }

    /**
     * 获取此协议支持的消息类型
     *
     * @return List
     */
    List<MqttMessageType> getMqttMessageTypes();

}
