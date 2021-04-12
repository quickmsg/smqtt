package com.github.smqtt.cluster.protocol;

import com.github.smqtt.common.channel.MqttChannel;
import com.github.smqtt.common.context.ReceiveContext;
import com.github.smqtt.common.interceptor.MessageInterceptor;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageType;

/**
 * @author luxurong
 * @date 2021/4/12 11:54
 * @description 集群适配器
 */
public class ClusterInterceptor implements MessageInterceptor {


    @Override
    public Object[] doInterceptor(Object[] args) {
        MqttChannel mqttChannel = (MqttChannel) args[0];
        MqttMessage mqttMessage = (MqttMessage) args[1];
        ReceiveContext<?> receiveContext = (ReceiveContext<?>) args[2];
        return new Object[0];
    }

    @Override
    public int order() {
        return 0;
    }

    @Override
    public MqttMessageType interceptorType() {
        return MqttMessageType.PUBLISH;
    }
}
