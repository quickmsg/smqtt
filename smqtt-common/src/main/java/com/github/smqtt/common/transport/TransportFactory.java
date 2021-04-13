package com.github.smqtt.common.transport;

import com.github.smqtt.common.config.Configuration;

/**
 * @author luxurong
 * @date 2021/3/30 19:28
 * @description
 */
public interface TransportFactory<C extends Configuration> {

    /**
     * 创建通道
     *
     * @param c 配置文件
     * @return Transport 通道类
     */
    Transport<C> createTransport(C c);


}
