package com.github.smqtt.core;

import com.github.smqtt.common.channel.MqttChannel;
import com.github.smqtt.common.context.ReceiveContext;
import com.github.smqtt.common.protocol.ProtocolAdaptor;
import com.github.smqtt.core.mqtt.MqttConfiguration;
import io.netty.handler.codec.mqtt.MqttMessage;

/**
 * @author luxurong
 * @date 2021/3/31 15:22
 * @description
 */
public class DefaultProtocolAdaptor implements ProtocolAdaptor<MqttConfiguration> {

    @Override
    public void chooseProtocol(MqttChannel mqttChannel, MqttMessage mqttMessage, ReceiveContext<MqttConfiguration> receiveContext) {

    }

}
