package com.github.smqtt.core;

import com.github.smqtt.common.channel.MqttChannel;
import com.github.smqtt.common.context.ReceiveContext;
import com.github.smqtt.common.protocol.Protocol;
import com.github.smqtt.common.protocol.ProtocolAdaptor;
import com.github.smqtt.common.spi.DynamicLoader;
import com.github.smqtt.core.mqtt.MqttConfiguration;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author luxurong
 * @date 2021/3/31 15:22
 * @description
 */
public class DefaultProtocolAdaptor implements ProtocolAdaptor<MqttConfiguration> {

    private Map<MqttMessageType, Protocol> types = new HashMap<>();

    public DefaultProtocolAdaptor() {
        DynamicLoader.findAll(Protocol.class)
                .forEach(protocol ->
                        protocol.getMqttMessageTypes().forEach(type -> {
                            MqttMessageType t = (MqttMessageType) type;
                            types.put(t, protocol);
                        }));

    }


    @Override
    public void chooseProtocol(MqttChannel mqttChannel, MqttMessage mqttMessage, ReceiveContext<MqttConfiguration> receiveContext) {
        Optional.ofNullable(types.get(mqttMessage.fixedHeader().messageType()))
                .ifPresent(protocol -> protocol.doParseProtocol(mqttMessage, mqttChannel, receiveContext));
    }


}
