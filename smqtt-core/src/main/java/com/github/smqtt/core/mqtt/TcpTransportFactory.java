package com.github.smqtt.core.mqtt;

import com.github.smqtt.common.transport.Transport;
import com.github.smqtt.common.transport.TransportFactory;
import com.github.smqtt.core.DefaultTransport;

/**
 * @author luxurong
 * @date 2021/3/30 19:55
 * @description
 */
public class TcpTransportFactory implements TransportFactory<TcpConfiguration> {

    @Override
    public Transport<TcpConfiguration> createTransport(TcpConfiguration config) {
        return new DefaultTransport(config ,new TcpReceiver());
    }
}
