package io.github.quickmsg.core.http;

import io.github.quickmsg.common.http.HttpActor;
import io.github.quickmsg.common.annotation.Header;
import io.github.quickmsg.common.annotation.Headers;
import io.github.quickmsg.common.annotation.Router;
import org.reactivestreams.Publisher;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;
import reactor.netty.http.server.HttpServerRoutes;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * @author luxurong
 */
public class HttpRouterAcceptor implements Consumer<HttpServerRoutes> {

    @Override
    public void accept(HttpServerRoutes httpServerRoutes) {
        HttpActor.INSTANCE.forEach(httpActor -> {
            Class<?> classt = httpActor.getClass();
            Router router = classt.getAnnotation(Router.class);
            BiFunction<? super HttpServerRequest, ? super HttpServerResponse, ? extends Publisher<Void>>
                    handler = (httpServerRequest, httpServerResponse) ->
                    this.doRequest(httpServerRequest, httpServerResponse, httpActor);
            switch (router.type()) {
                case PUT:
                    httpServerRoutes
                            .put(router.value(), handler);
                    break;
                case POST:
                    httpServerRoutes
                            .post(router.value(), handler);
                    break;
                case DELETE:
                    httpServerRoutes
                            .delete(router.value(), handler);
                    break;
                case GET:
                default:
                    httpServerRoutes
                            .get(router.value(), handler);
                    break;
            }
        });
    }

    private Publisher<Void> doRequest(HttpServerRequest httpServerRequest, HttpServerResponse httpServerResponse, HttpActor httpActor) {
        Header header = httpActor.getClass().getAnnotation(Header.class);
        Headers headers = httpActor.getClass().getAnnotation(Headers.class);
        Optional.ofNullable(header)
                .ifPresent(hd -> httpServerResponse.addHeader(hd.key(), hd.value()));
        Optional.ofNullable(headers)
                .ifPresent(hds -> Arrays.stream(hds.headers()).forEach(hd -> httpServerResponse.addHeader(hd.key(), hd.value())));
        return httpActor.doRequest(httpServerRequest, httpServerResponse);

    }


}
