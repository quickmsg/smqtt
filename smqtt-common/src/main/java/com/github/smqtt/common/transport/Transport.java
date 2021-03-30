package com.github.smqtt.common.transport;

import com.github.smqtt.common.config.Configuration;
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
     * @param c 启动参数
     * @return Disposable 连接操作类
     */
    Mono<Transport> start(C c);






}
