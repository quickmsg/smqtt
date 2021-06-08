package io.github.quickmsg.common.transport;

import io.github.quickmsg.common.config.Configuration;

/**
 * @author luxurong
 */
public interface TransportFactory<C extends Configuration> {

    /**
     * 创建通道
     *
     * @param c {@link Configuration}
     * @return {@link Transport}
     */
    Transport<C> createTransport(C c);


}
