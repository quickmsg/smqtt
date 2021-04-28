package com.github.quickmsg.core.protocol;

import com.github.quickmsg.common.channel.MqttChannel;
import com.github.quickmsg.common.protocol.Protocol;
import io.netty.handler.codec.mqtt.MqttMessageIdVariableHeader;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttPubAckMessage;
import reactor.core.publisher.Mono;
import reactor.util.context.ContextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author luxurong
 */
public class PublishAckProtocol implements Protocol<MqttPubAckMessage> {

    private static List<MqttMessageType> MESSAGE_TYPE_LIST = new ArrayList<>();


    static {
        MESSAGE_TYPE_LIST.add(MqttMessageType.PUBACK);
    }


    @Override
    public Mono<Void> parseProtocol(MqttPubAckMessage message, MqttChannel mqttChannel, ContextView contextView) {
        MqttMessageIdVariableHeader idVariableHeader = message.variableHeader();
        int messageId = idVariableHeader.messageId();
        return mqttChannel.cancelRetry(MqttMessageType.PUBLISH,messageId);
    }

    @Override
    public List<MqttMessageType> getMqttMessageTypes() {
        return MESSAGE_TYPE_LIST;
    }


}
