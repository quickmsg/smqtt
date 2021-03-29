package com.github.smqtt.common.protocol;

import reactor.core.publisher.Mono;

/**
 * @author luxurong
 * @date 2021/3/26 14:14
 * @description 具体协议消息类型处理
 */
public interface ProtocolTransport {


    /**
     * 处理协议
     *
     * @param message 消息类型
     * @return void
     */
    <T, U> Mono<U> transport(Mono<T> message);


}
