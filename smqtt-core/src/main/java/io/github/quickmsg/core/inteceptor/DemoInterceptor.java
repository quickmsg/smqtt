package io.github.quickmsg.core.inteceptor;

import io.github.quickmsg.common.channel.MqttChannel;
import io.github.quickmsg.common.context.ReceiveContext;
import io.github.quickmsg.common.interceptor.Interceptor;
import io.github.quickmsg.common.interceptor.Invocation;
import io.netty.handler.codec.mqtt.MqttMessage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author luxurong
 * @date 2021/6/8 15:35
 * @description
 */
public class DemoInterceptor implements Interceptor {


    @Override
    public Object intercept(Invocation invocation) {
        try {
            Method method = invocation.getMethod();
            Object[] args = invocation.getArgs();
            Object target = invocation.getTarget();
//            MqttChannel mqttChannel, MqttMessage mqttMessage, ReceiveContext<C> receiveContext

            MqttChannel mqttChannel = (MqttChannel) args[0]; // channel
            MqttMessage mqttMessage = (MqttMessage) args[1]; // MqttMessage
            ReceiveContext receiveContext = (ReceiveContext) args[2]; // ReceiveContext

            // 拦截业务

            return invocation.proceed(); // 放行
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int sort() {
        return 0;
    }
}
