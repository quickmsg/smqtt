package io.github.quickmsg.common.protocol;

import io.github.quickmsg.common.channel.MqttChannel;
import io.netty.handler.codec.mqtt.MqttMessageType;
import reactor.core.publisher.Mono;
import reactor.util.context.ContextView;

import java.util.List;

/**
 * @author luxurong
 */
public interface Protocol<T> {


    /**
     * 解析协议添加上下文
     *
     * @param message     消息类型
     * @param mqttChannel 通道
     * @return 空操作符
     */
    default Mono<Void> doParseProtocol(T message, MqttChannel mqttChannel) {
        return Mono.deferContextual(contextView -> this.parseProtocol(message, mqttChannel, contextView));
    }


    /**
     * 处理协议
     *
     * @param message     消息
     * @param mqttChannel 通道
     * @param contextView 上下文视图
     * @return 空操作符
     */
    Mono<Void> parseProtocol(T message, MqttChannel mqttChannel, ContextView contextView);


    /**
     * 获取此协议支持的消息类型
     *
     * @return List
     */
    List<MqttMessageType> getMqttMessageTypes();


}
