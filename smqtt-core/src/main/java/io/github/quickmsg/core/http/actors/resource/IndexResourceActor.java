package io.github.quickmsg.core.http.actors.resource;

import io.github.quickmsg.common.http.HttpActor;
import io.github.quickmsg.common.annotation.Router;
import io.github.quickmsg.common.enums.HttpType;
import org.reactivestreams.Publisher;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import java.nio.file.Paths;

/**
 * @author luxurong
 */
@Router(type = HttpType.GET, value = "/smqtt/admin")
public class IndexResourceActor implements HttpActor {

    private final static String DEFAULT_STATIC_PATH = "ui/dist/";

    @Override
    public Publisher<Void> doRequest(HttpServerRequest request, HttpServerResponse response) {
        String path= DEFAULT_STATIC_PATH+"index.html";
        return response.sendFile(Paths.get(path)).then();
    }
}
