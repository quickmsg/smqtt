package io.github.quickmsg.common.protocol;

import io.github.quickmsg.common.channel.MqttChannel;
import io.github.quickmsg.common.message.SmqttMessage;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageType;
import reactor.core.publisher.Mono;
import reactor.util.context.ContextView;

import java.util.List;

/**
 * @author luxurong
 */
public interface Protocol<T extends MqttMessage> {


    /**
     * 解析协议添加上下文
     *
     * @param message     {@link SmqttMessage}
     * @param mqttChannel {@link MqttChannel}
     * @return Mono
     * @see MqttMessage
     */
    default Mono<Void> doParseProtocol(SmqttMessage<T> message, MqttChannel mqttChannel) {
        return Mono.deferContextual(contextView -> this.parseProtocol(message, mqttChannel, contextView));
    }


    /**
     * 处理协议
     *
     * @param message     {@link SmqttMessage}
     * @param mqttChannel {@link MqttChannel}
     * @param contextView {@link ContextView}
     * @see MqttMessage
     * @return Mono
     */
    Mono<Void> parseProtocol(SmqttMessage<T> message, MqttChannel mqttChannel, ContextView contextView);


    /**
     * 获取此协议支持的消息类型
     *
     * @return {@link MqttMessageType}
     */
    List<MqttMessageType> getMqttMessageTypes();


}
