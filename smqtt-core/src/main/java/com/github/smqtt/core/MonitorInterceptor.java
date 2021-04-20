package com.github.smqtt.core;

import com.github.smqtt.common.interceptor.Interceptor;
import io.netty.handler.codec.mqtt.MqttMessageType;

/**
 * @author luxurong
 * @date 2021/4/12 15:00
 * @description
 */
public class MonitorInterceptor implements Interceptor {


    @Override
    public Object[] doInterceptor(Object[] args) {
        return args;
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
