package com.github.smqtt.common.Interceptor;

import com.github.smqtt.common.spi.DynamicLoader;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author luxurong
 * @date 2021/4/9 10:55
 * @description 针对push消息过滤
 */
public interface MessageInterceptor {

    List<MessageInterceptor> FILTER_LIST = DynamicLoader.findAll(MessageInterceptor.class)
            .collect(Collectors.toList());

    /**
     * 拦截目标参数
     *
     * @param args 参数
     * @return Object[]
     */
    Object[] doInterceptor(Object[] args);

}
