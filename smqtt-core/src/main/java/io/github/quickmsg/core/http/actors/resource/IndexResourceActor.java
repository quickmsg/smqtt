package io.github.quickmsg.core.http.actors.resource;

import io.github.quickmsg.common.http.HttpActor;
import io.github.quickmsg.common.annotation.Router;
import io.github.quickmsg.common.enums.HttpType;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Paths;

/**
 * @author luxurong
 */
@Router(type = HttpType.GET, value = "/smqtt/admin")
@Slf4j
public class IndexResourceActor implements HttpActor {

    private final static String DEFAULT_STATIC_PATH = "static/";

    @Override
    public Publisher<Void> doRequest(HttpServerRequest request, HttpServerResponse response) {
        String path=DEFAULT_STATIC_PATH+"index.html";
        log.info("index path {}",path);
        return response.sendFile(Paths.get(path)).then();
    }
}
