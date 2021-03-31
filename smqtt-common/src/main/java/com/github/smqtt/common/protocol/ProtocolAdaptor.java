package com.github.smqtt.common.protocol;

import com.github.smqtt.common.channel.MqttChannel;
import com.github.smqtt.common.config.Configuration;
import com.github.smqtt.common.context.ReceiveContext;
import io.netty.handler.codec.mqtt.MqttMessage;

/**
 * @author luxurong
 * @date 2021/3/31 10:59
 * @Description 协议分流器
 */
public interface ProtocolAdaptor<C extends Configuration> {


     void chooseProtocol(MqttChannel mqttChannel, MqttMessage mqttMessage, ReceiveContext<C> receiveContext);


}
