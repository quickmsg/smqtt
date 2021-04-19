package com.github.smqtt.core.http;

import com.github.smqtt.common.http.HttpActor;
import com.github.smqtt.common.http.annotation.Router;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

/**
 * @author luxurong
 * @date 2021/4/19 14:57
 * @description
 */
@Router("/smqtt/publish")
public class PublishActor implements HttpActor {

    @Override
    public Publisher<Void> doRequest(HttpServerRequest request, HttpServerResponse response) {
        return Mono.empty();
    }


}
