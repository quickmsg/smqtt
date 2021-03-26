package com.github.smqtt.protocol;

import io.netty.handler.codec.mqtt.MqttMessage;
import reactor.core.publisher.Mono;

/**
 * @author luxurong
 * @date 2021/3/26 13:55
 * @description 协议转换接口
 */
public interface Protocol {

    /**
     * 解析协议
     *
     * @param message 消息类型
     * @return void
     */
    <T> Mono<T> doParseProtocol(Mono<MqttMessage> message);




}
