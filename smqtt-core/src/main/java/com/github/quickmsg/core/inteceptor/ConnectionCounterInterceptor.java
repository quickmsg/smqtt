package com.github.quickmsg.core.inteceptor;

import com.github.quickmsg.common.context.ReceiveContext;
import com.github.quickmsg.common.interceptor.Interceptor;
import io.netty.handler.codec.mqtt.MqttMessageType;
import lombok.extern.slf4j.Slf4j;

/**
 * @author luxurong
 * @date 2021/4/20 19:06
 * @description
 */
@Slf4j
public class ConnectionCounterInterceptor implements Interceptor {


    @Override
    public Object[] doInterceptor(Object[] args) {
        ReceiveContext<?> receiveContexts = (ReceiveContext<?>) args[2];
        log.info(" client registry connection size {}", receiveContexts.getChannelRegistry().counts());
        return args;
    }

    @Override
    public int order() {
        return 0;
    }

    @Override
    public MqttMessageType interceptorType() {
        return MqttMessageType.CONNECT;
    }
}
