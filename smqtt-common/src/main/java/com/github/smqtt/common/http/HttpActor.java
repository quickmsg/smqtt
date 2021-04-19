package com.github.smqtt.common.http;

import com.github.smqtt.common.spi.DynamicLoader;
import org.reactivestreams.Publisher;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author luxurong
 * @date 2021/4/16 20:47
 * @description
 */
@FunctionalInterface
public interface HttpActor {


    List<HttpActor> INSTANCE = DynamicLoader.findAll(HttpActor.class).collect(Collectors.toList());


    /**
     * 处理
     *
     * @param request  请求
     * @param response 响应
     * @return Object
     */
    Publisher<Void> doRequest(HttpServerRequest request, HttpServerResponse response);


}
