package com.github.smqtt.common.transport;

import com.github.smqtt.common.config.Configuration;
import com.github.smqtt.common.spi.DynamicLoader;

/**
 * @author luxurong
 * @date 2021/3/30 19:28
 * @description
 */
public interface TransportFactory<C extends Configuration> {


    TransportFactory INSTANCE = DynamicLoader.findFirst(TransportFactory.class).orElse(null);


    /**
     * 创建这个类
     *
     * @param c 配置文件
     * @return Transport 通道类
     */
    Transport<C> createTransport(C c);


}
