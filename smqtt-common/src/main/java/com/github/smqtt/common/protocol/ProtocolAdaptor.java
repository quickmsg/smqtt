package com.github.smqtt.common.protocol;

import com.github.smqtt.common.channel.MqttChannel;
import com.github.smqtt.common.config.Configuration;
import com.github.smqtt.common.context.ReceiveContext;
import com.github.smqtt.common.spi.DynamicLoader;
import io.netty.handler.codec.mqtt.MqttMessage;

/**
 * @author luxurong
 * @date 2021/3/31 10:59
 * @Description 协议分流器
 */
public interface ProtocolAdaptor {

    ProtocolAdaptor INSTANCE = DynamicLoader.findFirst(ProtocolAdaptor.class).orElse(null);


    /**
     * 分发某种协议下  消息类型
     *
     * @param mqttChannel    通道
     * @param mqttMessage    消息
     * @param receiveContext 上下文
     * @return void
     */
    <C extends Configuration> void chooseProtocol(MqttChannel mqttChannel, MqttMessage mqttMessage, ReceiveContext<C> receiveContext);


}
