package io.github.quickmsg.core.http.actors.resource;

import io.github.quickmsg.common.annotation.Router;
import io.github.quickmsg.common.enums.HttpType;
import io.github.quickmsg.common.http.HttpActor;
import org.reactivestreams.Publisher;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import java.net.URI;
import java.nio.file.Paths;

/**
 * @author luxurong
 */
@Router(type = HttpType.GET, value = "/static/{path}/{path2}/{path3}")
public class ThreeResourceActor implements HttpActor {

    private final static String DEFAULT_STATIC_PATH = "static/";

    @Override
    public Publisher<Void> doRequest(HttpServerRequest request, HttpServerResponse response) {
        String path= this.getClass().getResource("/")+DEFAULT_STATIC_PATH+request.path();
        return response.sendFile(Paths.get(URI.create(path)));
    }
}
