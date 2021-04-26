package com.github.quickmsg.core.protocol;

import com.github.quickmsg.common.channel.MqttChannel;
import com.github.quickmsg.common.context.ReceiveContext;
import com.github.quickmsg.common.message.MqttMessageBuilder;
import com.github.quickmsg.common.protocol.Protocol;
import com.github.quickmsg.common.topic.TopicRegistry;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttUnsubscribeMessage;
import reactor.core.publisher.Mono;
import reactor.util.context.ContextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author luxurong
 * @date 2021/3/29 14:05
 * @description server 处理
 */
public class UnSubscribeProtocol implements Protocol<MqttUnsubscribeMessage> {

    private static List<MqttMessageType> MESSAGE_TYPE_LIST = new ArrayList<>();

    static {
        MESSAGE_TYPE_LIST.add(MqttMessageType.UNSUBSCRIBE);
    }


    @Override
    public Mono<Void> parseProtocol(MqttUnsubscribeMessage message, MqttChannel mqttChannel, ContextView contextView) {
        return Mono.fromRunnable(() -> {
            ReceiveContext<?> receiveContext = contextView.get(ReceiveContext.class);
            TopicRegistry topicRegistry = receiveContext.getTopicRegistry();
            topicRegistry.clear(message.payload().topics(), mqttChannel);
        }).then(mqttChannel.write(MqttMessageBuilder.buildUnsubAck(message.variableHeader().messageId()), false));
    }

    @Override
    public List<MqttMessageType> getMqttMessageTypes() {
        return MESSAGE_TYPE_LIST;
    }


}
