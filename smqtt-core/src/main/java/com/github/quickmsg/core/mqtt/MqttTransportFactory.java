package com.github.quickmsg.core.mqtt;

import com.github.quickmsg.core.DefaultTransport;
import com.github.quickmsg.common.transport.Transport;
import com.github.quickmsg.common.transport.TransportFactory;

/**
 * @author luxurong
 */
public class MqttTransportFactory implements TransportFactory<MqttConfiguration> {

    @Override
    public Transport<MqttConfiguration> createTransport(MqttConfiguration config) {
        return new DefaultTransport(config ,new MqttReceiver());
    }
}
