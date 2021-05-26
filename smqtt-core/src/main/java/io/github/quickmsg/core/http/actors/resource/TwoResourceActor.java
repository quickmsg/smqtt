package io.github.quickmsg.core.http.actors.resource;

import io.github.quickmsg.common.http.HttpActor;
import io.github.quickmsg.common.http.annotation.Router;
import io.github.quickmsg.common.http.enums.HttpType;
import org.reactivestreams.Publisher;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import java.nio.file.Paths;

/**
 * @author luxurong
 */
@Router(type = HttpType.GET, value = "/static/{path}/{path2}")
public class TwoResourceActor implements HttpActor {

    private final static String DEFAULT_STATIC_PATH = "ui/dist/static/";

    @Override
    public Publisher<Void> doRequest(HttpServerRequest request, HttpServerResponse response) {
        String path= DEFAULT_STATIC_PATH+request.param("path")+"/"+request.param("path2");
        return response.sendFile(Paths.get(path));
    }
}
