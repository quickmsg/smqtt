package com.github.quickmsg.core;

import com.github.quickmsg.common.interceptor.Interceptor;
import io.netty.handler.codec.mqtt.MqttMessageType;

/**
 * @author luxurong
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
