package io.github.quickmsg.core.http;

import io.github.quickmsg.common.transport.Transport;
import io.github.quickmsg.common.transport.TransportFactory;

/**
 * @author luxurong
 */
public class HttpTransportFactory implements TransportFactory<HttpConfiguration> {


    @Override
    public Transport<HttpConfiguration> createTransport(HttpConfiguration httpConfiguration) {
        return new HttpTransport(httpConfiguration, new HttpReceiver());
    }
}
