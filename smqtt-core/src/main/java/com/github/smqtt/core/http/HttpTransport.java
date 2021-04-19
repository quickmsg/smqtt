package com.github.smqtt.core.http;

import com.github.smqtt.common.Receiver;
import com.github.smqtt.common.context.ReceiveContext;
import com.github.smqtt.common.transport.Transport;
import lombok.AllArgsConstructor;
import lombok.Getter;
import reactor.core.publisher.Mono;

/**
 * @author luxurong
 * @date 2021/4/19 17:28
 * @description
 */
@AllArgsConstructor
@Getter
public class HttpTransport implements Transport<HttpConfiguration> {


    private HttpConfiguration configuration;

    private Receiver receiver;


    @Override
    public Mono<Transport> start() {
        return null;
    }

    @Override
    public ReceiveContext<HttpConfiguration> buildReceiveContext(HttpConfiguration httpConfiguration) {
        throw new UnsupportedOperationException("no support buildReceiveContext ");
    }

    @Override
    public void dispose() {

    }
}
