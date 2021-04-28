package io.github.quickmsg.core.mqtt;

import io.github.quickmsg.core.DefaultTransport;
import io.github.quickmsg.common.transport.Transport;
import io.github.quickmsg.common.transport.TransportFactory;

/**
 * @author luxurong
 */
public class MqttTransportFactory implements TransportFactory<MqttConfiguration> {

    @Override
    public Transport<MqttConfiguration> createTransport(MqttConfiguration config) {
        return new DefaultTransport(config ,new MqttReceiver());
    }
}
