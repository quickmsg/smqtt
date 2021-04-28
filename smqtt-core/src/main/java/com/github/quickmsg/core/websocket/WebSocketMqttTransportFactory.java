package com.github.quickmsg.core.websocket;

import com.github.quickmsg.common.transport.Transport;
import com.github.quickmsg.common.transport.TransportFactory;
import com.github.quickmsg.core.DefaultTransport;
import com.github.quickmsg.core.mqtt.MqttConfiguration;

/**
 * @author luxurong
 */
public class WebSocketMqttTransportFactory implements TransportFactory<MqttConfiguration> {

    @Override
    public Transport<MqttConfiguration> createTransport(MqttConfiguration config) {
        return new DefaultTransport(config, new WebSocketMqttReceiver());
    }


}
