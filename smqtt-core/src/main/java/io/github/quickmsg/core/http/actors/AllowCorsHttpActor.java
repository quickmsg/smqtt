package io.github.quickmsg.core.http.actors;

import io.github.quickmsg.common.annotation.AllowCors;
import io.github.quickmsg.common.annotation.Router;
import io.github.quickmsg.common.config.Configuration;
import io.github.quickmsg.common.enums.HttpType;
import io.github.quickmsg.common.http.HttpActor;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

/**
 * @author luxurong
 */

@Router(value = "/**", type = HttpType.OPTIONS)
@AllowCors
public class AllowCorsHttpActor implements HttpActor {
    @Override
    public Publisher<Void> doRequest(HttpServerRequest request, HttpServerResponse response, Configuration configuration) {
        return Mono.empty();
    }
}
