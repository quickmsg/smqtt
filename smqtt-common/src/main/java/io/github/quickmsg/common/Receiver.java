package io.github.quickmsg.common;

import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;

/**
 * @author luxurong
 */
public interface Receiver {

    /**
     * 绑定接口
     *
     * @return  {@link DisposableServer}
     */
    Mono<DisposableServer> bind();


}
