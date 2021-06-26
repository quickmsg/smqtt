package io.github.quickmsg.core.http.actors.resource;

import io.github.quickmsg.common.annotation.Router;
import io.github.quickmsg.common.config.Configuration;
import io.github.quickmsg.common.enums.HttpType;
import io.github.quickmsg.common.http.HttpActor;
import io.github.quickmsg.common.utils.ClassPathLoader;
import io.github.quickmsg.core.http.HttpConfiguration;
import org.reactivestreams.Publisher;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

/**
 * @author luxurong
 */
@Router(type = HttpType.GET, value = "/static/**",resource = true)
public class StaticResourceActor implements HttpActor {

    private final static String DEFAULT_STATIC_PATH = "/static/";

    @Override
    public Publisher<Void> doRequest(HttpServerRequest request, HttpServerResponse response, Configuration httpConfiguration) {
        String path = DEFAULT_STATIC_PATH + request.path();
        return response.send(ClassPathLoader.readClassPathFile(path)).then();
    }
}
