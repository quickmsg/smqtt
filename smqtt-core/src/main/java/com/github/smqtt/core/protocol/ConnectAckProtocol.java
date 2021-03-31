package com.github.smqtt.core.protocol;

import com.github.smqtt.common.channel.MqttChannel;
import com.github.smqtt.common.protocol.Protocol;
import io.netty.handler.codec.mqtt.MqttConnAckMessage;
import io.netty.handler.codec.mqtt.MqttMessageType;
import reactor.core.publisher.Mono;
import reactor.util.context.ContextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author luxurong
 * @date 2021/3/29 14:05
 * @description
 */
public class ConnectAckProtocol implements Protocol<MqttConnAckMessage> {


    private static List<MqttMessageType> MESSAGE_TYPE_LIST = new ArrayList<>();

    static {
        MESSAGE_TYPE_LIST.add(MqttMessageType.CONNACK);
    }


    @Override
    public Mono<Void> parseProtocol(MqttConnAckMessage message, MqttChannel mqttChannel, ContextView contextView) {
        return null;
    }

    @Override
    public List<MqttMessageType> getMqttMessageTypes() {
        return MESSAGE_TYPE_LIST;
    }


}
