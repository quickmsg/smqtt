package com.github.smqtt.common.message;

import reactor.core.publisher.Sinks;

/**
 * @author luxurong
 * @date 2021/4/2 16:42
 * @description 处理具体消息发送的管道
 */
public interface MessageSink<T> extends Sinks.Many<T> {


}
