package com.github.quickmsg.core.http;

import com.github.quickmsg.common.transport.Transport;
import com.github.quickmsg.common.transport.TransportFactory;

/**
 * @author luxurong
 * @date 2021/4/19 16:44
 * @description
 */
public class HttpTransportFactory implements TransportFactory<HttpConfiguration> {


    @Override
    public Transport<HttpConfiguration> createTransport(HttpConfiguration httpConfiguration) {
        return new HttpTransport(httpConfiguration, new HttpReceiver());
    }
}
