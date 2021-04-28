package io.github.quickmsg.core.http;

import io.github.quickmsg.common.http.HttpActor;
import io.github.quickmsg.common.http.annotation.Router;
import reactor.netty.http.server.HttpServerRoutes;

import java.util.function.Consumer;

/**
 * @author luxurong
 */
public class HttpRouterAcceptor implements Consumer<HttpServerRoutes> {

    @Override
    public void accept(HttpServerRoutes httpServerRoutes) {
        HttpActor.INSTANCE.forEach(httpActor -> {
            Router router = httpActor.getClass().getAnnotation(Router.class);
            switch (router.type()) {
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
                case GET:
                default:
                    httpServerRoutes
                            .get(router.value(), httpActor::doRequest);
                    break;
            }
        });
    }


}
