package com.github.smqtt.core.http;

import com.github.smqtt.common.http.HttpActor;
import com.github.smqtt.common.http.annotation.Router;
import com.github.smqtt.common.http.enums.HttpType;
import com.github.smqtt.common.message.HttpPublishMessage;
import com.github.smqtt.common.protocol.ProtocolAdaptor;
import com.github.smqtt.core.DefaultTransport;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

/**
 * @author luxurong
 * @date 2021/4/19 14:57
 * @description
 */
@Router(value = "/smqtt/publish", type = HttpType.POST)
@Slf4j
public class PublishActor implements HttpActor {


    @Override
    public Publisher<Void> doRequest(HttpServerRequest request, HttpServerResponse response) {
        return request
                .receive()
                .asString()
                .map(this.toJson(HttpPublishMessage.class))
                .doOnNext(message -> {
                    ProtocolAdaptor protocolAdaptor = DefaultTransport.receiveContext.getProtocolAdaptor();
                    protocolAdaptor.chooseProtocol(DEFAULT_MOCK_CHANNEL, message.getPublishMessage(), DefaultTransport.receiveContext);
                    log.info("http request url {} body {}", request.path(), message);
                }).then(response.sendString(Mono.just("success")).then());
    }


}
