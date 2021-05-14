package io.github.quickmsg.core.http.actors;

import com.alibaba.fastjson.JSON;
import io.github.quickmsg.common.channel.MqttChannel;
import io.github.quickmsg.common.enums.ChannelStatus;
import io.github.quickmsg.common.http.annotation.Router;
import io.github.quickmsg.common.http.enums.HttpType;
import io.github.quickmsg.common.message.HttpPublishMessage;
import io.github.quickmsg.core.DefaultTransport;
import io.github.quickmsg.core.http.AbstractHttpActor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.Connection;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import java.util.Set;

/**
 * @author luxurong
 */
@Router(value = "/smqtt/Connection", type = HttpType.POST)
@Slf4j
public class ConnectionActor extends AbstractHttpActor {

    @Override
    public Publisher<Void> doRequest(HttpServerRequest request, HttpServerResponse response) {
        response.addHeader("Content-Type", "application/json");
        return request
                .receive()
                .then(response
                        .sendString(Mono.just(JSON.toJSONString(DefaultTransport.receiveContext.getChannelRegistry().getChannels())))
                        .then());
    }



}
