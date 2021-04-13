package com.github.smqtt.core.websocket;

import com.github.smqtt.common.transport.Transport;
import com.github.smqtt.common.transport.TransportFactory;
import com.github.smqtt.core.DefaultTransport;
import com.github.smqtt.core.mqtt.MqttConfiguration;

/**
 * @author luxurong
 * @date 2021/3/30 19:55
 * @description
 */
public class WebSocketMqttTransportFactory implements TransportFactory<MqttConfiguration> {

    @Override
    public Transport<MqttConfiguration> createTransport(MqttConfiguration config) {
        return new DefaultTransport(config, new WebSocketMqttReceiver());
    }


}
