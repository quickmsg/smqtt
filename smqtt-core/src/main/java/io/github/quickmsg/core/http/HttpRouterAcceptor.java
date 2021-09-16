package io.github.quickmsg.core.http;

import io.github.quickmsg.common.annotation.AllowCors;
import io.github.quickmsg.common.annotation.Header;
import io.github.quickmsg.common.annotation.Headers;
import io.github.quickmsg.common.annotation.Router;
import io.github.quickmsg.common.http.HttpActor;
import io.netty.handler.codec.http.HttpHeaderNames;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;
import reactor.netty.http.server.HttpServerRoutes;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * @author luxurong
 */
public class HttpRouterAcceptor implements Consumer<HttpServerRoutes> {

    private final HttpConfiguration httpConfiguration;

    public HttpRouterAcceptor(HttpConfiguration httpConfiguration) {
        this.httpConfiguration = httpConfiguration;
    }

    @Override
    public void accept(HttpServerRoutes httpServerRoutes) {
        HttpActor.INSTANCE.forEach(httpActor -> {
            Class<?> classt = httpActor.getClass();
            Router router = classt.getAnnotation(Router.class);
            BiFunction<? super HttpServerRequest, ? super HttpServerResponse, ? extends Publisher<Void>>
                    handler = (httpServerRequest, httpServerResponse) ->
                    this.doRequest(httpServerRequest, httpServerResponse, httpActor, router);
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
                case OPTIONS:
                    httpServerRoutes
                            .options(router.value(), handler);
                    break;
                case GET:
                default:
                    httpServerRoutes
                            .get(router.value(), handler);
                    break;
            }
        });
    }

    private Publisher<Void> doRequest(HttpServerRequest httpServerRequest, HttpServerResponse httpServerResponse, HttpActor httpActor, Router router) {
        Header header = httpActor.getClass().getAnnotation(Header.class);
        Headers headers = httpActor.getClass().getAnnotation(Headers.class);
        AllowCors allowCors = httpActor.getClass().getAnnotation(AllowCors.class);
        if (router.resource() && !httpConfiguration.getEnableAdmin()) {
            return Mono.empty();
        } else {
            if (header != null) {
                httpServerResponse.addHeader(header.key(), header.value());
            }
            if (headers != null) {
                Arrays.stream(headers.headers()).forEach(hd -> httpServerResponse.addHeader(hd.key(), hd.value()));
            }
            if (allowCors != null) {
                httpServerResponse.addHeader(HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS, "*");
                httpServerResponse.addHeader(HttpHeaderNames.ACCESS_CONTROL_ALLOW_METHODS, "*");
                httpServerResponse.addHeader(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
            }
            return httpActor.doRequest(httpServerRequest, httpServerResponse, httpConfiguration);
        }
    }


}
