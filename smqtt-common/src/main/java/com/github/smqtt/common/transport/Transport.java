package com.github.smqtt.common.transport;

import com.github.smqtt.common.config.Configuration;
import com.github.smqtt.common.context.ReceiveContext;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

/**
 * @author luxurong
 * @date 2021/3/29 11:21
 * @description
 */
public interface Transport<C extends Configuration> extends Disposable {


    /**
     * 开启连接
     *
     * @return Disposable 连接操作类
     */
    Mono<Transport> start();


    /**
     * 构建接受处理🥱
     *
     * @param c 启动参数
     * @return ReceiveContext
     */
    ReceiveContext<C> buildReceiveContext(C c);


    /**
     * 链接注册中心
     *
     * @return ChannelRegistry
     */
    ReceiveContext<C> getReceiveContext();



    /**
     * 复制Transport 用于共享通道
     *
     * @return ChannelRegistry
     */
    Transport<C> copy();


}
