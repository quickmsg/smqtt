package com.github.smqtt.core.protocol;

import com.github.smqtt.common.channel.MqttChannel;
import com.github.smqtt.common.message.MqttMessageBuilder;
import com.github.smqtt.common.protocol.Protocol;
import io.netty.handler.codec.mqtt.MqttConnAckMessage;
import io.netty.handler.codec.mqtt.MqttMessageType;
import reactor.core.publisher.Mono;
import reactor.util.context.ContextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author luxurong
 * @date 2021/4/1 20:08
 * @description client && server 处理
 */
public class CommonProtocol implements Protocol<MqttConnAckMessage> {


    private static List<MqttMessageType> MESSAGE_TYPE_LIST = new ArrayList<>();

    static {
        MESSAGE_TYPE_LIST.add(MqttMessageType.PINGRESP);
        MESSAGE_TYPE_LIST.add(MqttMessageType.PINGREQ);
        MESSAGE_TYPE_LIST.add(MqttMessageType.DISCONNECT);
    }


    @Override
    public Mono<Void> parseProtocol(MqttConnAckMessage message, MqttChannel mqttChannel, ContextView contextView) {
        switch (message.fixedHeader().messageType()) {
            case PINGREQ:
                return mqttChannel.write(MqttMessageBuilder.buildPongMessage(),false);
            case DISCONNECT:
                mqttChannel.setWill(null);
                mqttChannel.getConnection().disposeNow();
                return Mono.empty();
            case PINGRESP:
            default:
                return Mono.empty();

        }
    }

    @Override
    public List<MqttMessageType> getMqttMessageTypes() {
        return MESSAGE_TYPE_LIST;
    }
}
