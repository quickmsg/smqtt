package io.github.quickmsg.core.protocol;

import io.github.quickmsg.common.ack.Ack;
import io.github.quickmsg.common.channel.MqttChannel;
import io.github.quickmsg.common.context.ReceiveContext;
import io.github.quickmsg.common.message.SmqttMessage;
import io.github.quickmsg.common.protocol.Protocol;
import io.netty.handler.codec.mqtt.MqttConnectMessage;
import io.netty.handler.codec.mqtt.MqttMessageIdVariableHeader;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttPubAckMessage;
import reactor.core.publisher.Mono;
import reactor.util.context.ContextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author luxurong
 */
public class PublishAckProtocol implements Protocol<MqttPubAckMessage> {

    private static List<MqttMessageType> MESSAGE_TYPE_LIST = new ArrayList<>();


    static {
        MESSAGE_TYPE_LIST.add(MqttMessageType.PUBACK);
    }


    @Override
    public Mono<Void> parseProtocol(SmqttMessage<MqttPubAckMessage> smqttMessage, MqttChannel mqttChannel, ContextView contextView) {
        return Mono.fromRunnable(()->{
            ReceiveContext<?> receiveContext = contextView.get(ReceiveContext.class);
            MqttPubAckMessage message = smqttMessage.getMessage();
            MqttMessageIdVariableHeader idVariableHeader = message.variableHeader();
            int messageId = idVariableHeader.messageId();
            Optional.ofNullable(receiveContext.getTimeAckManager().getAck(mqttChannel.generateId(MqttMessageType.PUBLISH,messageId)))
                    .ifPresent(Ack::stop);
        });
    }

    @Override
    public List<MqttMessageType> getMqttMessageTypes() {
        return MESSAGE_TYPE_LIST;
    }


}
