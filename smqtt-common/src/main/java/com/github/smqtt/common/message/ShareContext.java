package com.github.smqtt.common.message;

import com.github.smqtt.common.context.ReceiveContext;

import java.util.function.Function;

/**
 * @author luxurong
 * @date 2021/4/12 21:59
 * @description
 */
public interface ShareContext<T> {


    void shareTransport(ReceiveContext<?> r, Function<T, Object> tansfer);


    <R> void acceptShare(Object obj);


}
