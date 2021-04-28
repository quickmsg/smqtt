package io.github.quickmsg.core.websocket;

import io.github.quickmsg.common.transport.Transport;
import io.github.quickmsg.common.transport.TransportFactory;
import io.github.quickmsg.core.DefaultTransport;
import io.github.quickmsg.core.mqtt.MqttConfiguration;

/**
 * @author luxurong
 */
public class WebSocketMqttTransportFactory implements TransportFactory<MqttConfiguration> {

    @Override
    public Transport<MqttConfiguration> createTransport(MqttConfiguration config) {
        return new DefaultTransport(config, new WebSocketMqttReceiver());
    }


}
