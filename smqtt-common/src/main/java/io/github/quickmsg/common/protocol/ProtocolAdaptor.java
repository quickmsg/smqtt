package io.github.quickmsg.common.protocol;

import io.github.quickmsg.common.config.Configuration;
import io.github.quickmsg.common.interceptor.Interceptor;
import io.github.quickmsg.common.channel.MqttChannel;
import io.github.quickmsg.common.context.ReceiveContext;
import io.github.quickmsg.common.spi.DynamicLoader;
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
 */
public interface ProtocolAdaptor {

    ProtocolAdaptor INSTANCE = DynamicLoader.findFirst(ProtocolAdaptor.class).orElse(null);


    Map<MqttMessageType, List<Interceptor>> INTERCEPTORS = new ConcurrentHashMap<>();


    /**
     * 分发某种协议下  消息类型
     *
     * @param mqttChannel    通道
     * @param mqttMessage    消息
     * @param receiveContext 上下文
     * @param <C> 配置文件
     */
    <C extends Configuration> void chooseProtocol(MqttChannel mqttChannel, MqttMessage mqttMessage, ReceiveContext<C> receiveContext);


    /**
     * 代理类  用来注入 filter monitor
     *
     * @return 适配器
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
     * @return 代理类
     * @throws InvocationTargetException 代理异常
     * @throws IllegalAccessException 非法状态异常
     */
    default Object intercept(Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        MqttMessage mqttMessage = (MqttMessage) args[1];
        if (mqttMessage.fixedHeader() != null) {
            List<Interceptor> interceptors =
                    INTERCEPTORS.computeIfAbsent(mqttMessage.fixedHeader().messageType(), mqttMessageType ->
                            Interceptor.FILTER_LIST.stream()
                                    .filter(messageInterceptor -> messageInterceptor.interceptorType() == mqttMessage.fixedHeader().messageType())
                                    .collect(Collectors.toList()));
            for (Interceptor interceptor : interceptors) {
                args = interceptor.doInterceptor(args);
            }
            return method.invoke(this, args);
        } else {
            return null;
        }
    }


}
