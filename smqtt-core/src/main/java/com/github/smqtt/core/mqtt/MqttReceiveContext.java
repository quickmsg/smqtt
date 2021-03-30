package com.github.smqtt.core.mqtt;

import com.github.smqtt.common.context.ReceiveContext;
import com.github.smqtt.common.transport.Transport;

/**
 * @author luxurong
 * @date 2021/3/30 13:25
 * @description
 */
public class MqttReceiveContext extends ReceiveContext<MqttConfiguration> {

    public MqttReceiveContext(MqttConfiguration configuration, Transport<MqttConfiguration> transport) {
        super(configuration, transport);
    }



}
