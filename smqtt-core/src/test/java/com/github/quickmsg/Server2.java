package com.github.quickmsg;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.netty.ByteBufFlux;
import reactor.netty.http.client.HttpClient;

import java.util.concurrent.CountDownLatch;

/**
 * @author luxurong
 * @date 2021/3/26 16:24
 * @description
 */
public class Server2 {

    @Test
    public void test() throws InterruptedException {
//        DisposableServer server= HttpServer.create().port(11113).route(routes ->
//                routes.post("/test/{param}", (req, res) ->
//                        res.sendString(req.receive()
//                                .asString()
//                                .map(s -> {
//                                    return s + ' ' + req.param("param") + '!';
//                                })))).bindNow();

        HttpClient.create()             // Prepares a HTTP client for configuration.
                .port(18997)  // Obtain the server's port and provide it as a port to which this
                // client should connect.
                .wiretap(true)            // Applies a wire logger configuration.
                .headers(h -> h.add("Content-Type", "text/plain")) // Adds headers to the HTTP request.
                .post()              // Specifies that POST method will be used.
                .uri("/smqtt/publish/cejjksa")  // Specifies the path.
                .send(ByteBufFlux.fromString(Flux.just("Hello")))  // Sends the request body.
                .responseContent()   // Receives the response body.
                .aggregate()
                .asString()
                .block();

        CountDownLatch latch = new CountDownLatch(1);
        latch.await();
    }

}
