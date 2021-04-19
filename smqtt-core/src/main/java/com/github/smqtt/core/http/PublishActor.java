package com.github.smqtt.core.http;

import com.github.smqtt.common.http.HttpActor;
import com.github.smqtt.common.http.annotation.Router;
import com.github.smqtt.common.message.HttpPublishMessage;
import com.github.smqtt.core.DefaultTransport;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

/**
 * @author luxurong
 * @date 2021/4/19 14:57
 * @description
 */
@Router("/smqtt/publish")
public class PublishActor implements HttpActor {


    @Override
    public void doRequest(HttpServerRequest request, HttpServerResponse response) {
        request.receiveContent()
                .cast(HttpPublishMessage.class)
                .subscribe(httpPublishMessage -> {
//                        this.transportHttpMessage(httpPublishMessage)
                });
    }

    private void transportHttpMessage(HttpPublishMessage httpPublishMessage) {
//        DefaultTransport.receiveContext.getProtocolAdaptor()
//                .chooseProtocol();
    }


}
