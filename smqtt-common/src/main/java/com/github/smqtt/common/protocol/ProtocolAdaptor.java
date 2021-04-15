package com.github.smqtt.common.protocol;

import com.github.smqtt.common.channel.MqttChannel;
import com.github.smqtt.common.config.Configuration;
import com.github.smqtt.common.context.ReceiveContext;
import com.github.smqtt.common.interceptor.MessageInterceptor;
import com.github.smqtt.common.spi.DynamicLoader;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author luxurong
 * @date 2021/3/31 10:59
 * @Description 协议分流器
 */
public interface ProtocolAdaptor {

    ProtocolAdaptor INSTANCE = DynamicLoader.findFirst(ProtocolAdaptor.class).orElse(null);


    Map<MqttMessageType, List<MessageInterceptor>> INTERCEPTORS = new ConcurrentHashMap<>();


    /**
     * 分发某种协议下  消息类型
     *
     * @param mqttChannel    通道
     * @param mqttMessage    消息
     * @param receiveContext 上下文
     * @return void
     */
    <C extends Configuration> void chooseProtocol(MqttChannel mqttChannel, MqttMessage mqttMessage, ReceiveContext<C> receiveContext);


    /**
     * 代理类  用来注入 filter monitor
     *
     * @return ProtocolAdaptor
     */
    default ProtocolAdaptor proxy() {
        return (ProtocolAdaptor) Proxy.newProxyInstance(ProtocolAdaptor.class.getClassLoader(),
                new Class[]{ProtocolAdaptor.class},
                (proxy, method, args) -> intercept(method, args));
    }


    /**
     * 拦截
     *
     * @param method 方法
     * @param args   参数
     * @return Object
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    default Object intercept(Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        MqttMessage mqttMessage = (MqttMessage) args[1];
        if (mqttMessage.fixedHeader() != null) {
            List<MessageInterceptor> interceptors =
                    INTERCEPTORS.computeIfAbsent(mqttMessage.fixedHeader().messageType(), mqttMessageType ->
                            MessageInterceptor.FILTER_LIST.stream()
                                    .filter(messageInterceptor -> messageInterceptor.interceptorType() == mqttMessage.fixedHeader().messageType())
                                    .collect(Collectors.toList()));
            for (MessageInterceptor interceptor : interceptors) {
                args = interceptor.doInterceptor(args);
            }
            return method.invoke(this, args);
        } else {
            return null;
        }
    }


}
