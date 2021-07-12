package io.github.quickmsg.core.http.actors;

import com.alibaba.fastjson.JSON;
import io.github.quickmsg.common.annotation.AllowCors;
import io.github.quickmsg.common.annotation.Header;
import io.github.quickmsg.common.annotation.Router;
import io.github.quickmsg.common.config.Configuration;
import io.github.quickmsg.common.enums.HttpType;
import io.github.quickmsg.core.DefaultTransport;
import io.github.quickmsg.core.http.AbstractHttpActor;
import io.github.quickmsg.core.http.HttpConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

/**
 * @author luxurong
 */
@Router(value = "/smqtt/connection", type = HttpType.POST)
@Slf4j
@Header(key = "Content-Type", value = "application/json")
@AllowCors
public class ConnectionActor extends AbstractHttpActor {

    @Override
    public Publisher<Void> doRequest(HttpServerRequest request, HttpServerResponse response, Configuration httpConfiguration) {
        return request
                .receive()
                .then(response
                        .sendString(Mono.just(JSON.toJSONString(DefaultTransport.receiveContext.getChannelRegistry().getChannels())))
                        .then());
    }



}
