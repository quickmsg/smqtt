package com.github.smqtt.core.mqtt;

import com.github.smqtt.common.ReceiveContext;
import com.github.smqtt.common.transport.Transport;

/**
 * @author luxurong
 * @date 2021/3/30 13:25
 * @description
 */
public class TcpReceiveContext extends ReceiveContext<TcpConfiguration> {

    public TcpReceiveContext(TcpConfiguration configuration, Transport<TcpConfiguration> transport) {
        super(configuration, transport);
    }



}
