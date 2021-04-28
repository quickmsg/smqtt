package io.github.quickmsg.common.transport;

import io.github.quickmsg.common.config.Configuration;

/**
 * @author luxurong
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
