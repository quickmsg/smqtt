package com.github.smqtt.common;

import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;

/**
 * @author luxurong
 * @date 2021/3/29 20:09
 * @description
 */
public interface Receiver {

    /**
     * 绑定接口
     *
     * @return DisposableServer
     */
    Mono<DisposableServer> bind();

}
