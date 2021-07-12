package io.github.quickmsg.common.transport;

import io.github.quickmsg.common.config.Configuration;
import io.github.quickmsg.common.context.ReceiveContext;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

/**
 * @author luxurong
 */
public interface Transport<C extends Configuration> extends Disposable {


    /**
     * 开启连接
     *
     * @return {@link Transport}
     */
    Mono<Transport> start();


    /**
     * 构建接受处理🥱
     *
     * @param c {@link Configuration}
     * @return {@link ReceiveContext}
     */
    ReceiveContext<C> buildReceiveContext(C c);


}
