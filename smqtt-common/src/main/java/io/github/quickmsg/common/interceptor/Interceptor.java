package io.github.quickmsg.common.interceptor;

import io.github.quickmsg.common.spi.DynamicLoader;
import io.netty.handler.codec.mqtt.MqttMessageType;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author luxurong
 * @date 2021/4/9 10:55
 * @description 针对push消息过滤
 */
public interface Interceptor {

    List<Interceptor> FILTER_LIST = DynamicLoader.findAll(Interceptor.class)
            .sorted(Comparator.comparing(Interceptor::order))
            .collect(Collectors.toList());

    /**
     * 拦截目标参数
     *
     * @param args 参数
     * @return Object[]
     */
    Object[] doInterceptor(Object[] args);


    /**
     * 排序
     *
     * @return int
     */
    int order();


    /**
     * 拦截消息类型
     *
     * @return MqttMessageType
     */
    MqttMessageType interceptorType();


}
