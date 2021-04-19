package com.github.smqtt.core.http;

import com.github.smqtt.common.http.HttpActor;
import com.github.smqtt.common.http.annotation.Router;
import reactor.netty.http.server.HttpServerRoutes;

import java.util.function.Consumer;

/**
 * @author luxurong
 * @date 2021/4/19 15:10
 * @description 处理http协议
 */
public class HttpRouterAcceptor implements Consumer<HttpServerRoutes> {

    @Override
    public void accept(HttpServerRoutes httpServerRoutes) {
        HttpActor.INSTANCE
                .forEach(httpActor -> {
                    Router router = httpActor.getClass().getAnnotation(Router.class);
                    switch (router.type()) {
                        case GET:
                            httpServerRoutes
                                    .get(router.value(), httpActor::doRequest);
                            break;
                        case PUT:
                            httpServerRoutes
                                    .put(router.value(), httpActor::doRequest);
                            break;
                        case POST:
                            httpServerRoutes
                                    .post(router.value(), httpActor::doRequest);
                            break;
                        case DELETE:
                            httpServerRoutes
                                    .delete(router.value(), httpActor::doRequest);
                            break;
                        default:
                    }
                });
    }


}
