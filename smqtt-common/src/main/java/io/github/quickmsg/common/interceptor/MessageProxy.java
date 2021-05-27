package io.github.quickmsg.common.interceptor;

import io.github.quickmsg.common.protocol.ProtocolAdaptor;
import io.github.quickmsg.common.spi.DynamicLoader;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author luxurong
 * @date 2021/5/27 11:53
 * @description
 */
public class MessageProxy {

    private List<Interceptor> interceptors = DynamicLoader.findAll(Interceptor.class)
            .sorted(Comparator.comparing(Interceptor::sort))
            .collect(Collectors.toList());

    public  ProtocolAdaptor proxy(ProtocolAdaptor protocolAdaptor) {
        if (interceptors.size() > 0) {
            for (Interceptor interceptor : interceptors) {
                protocolAdaptor = interceptor.proxyProtocol(protocolAdaptor, interceptor);
            }
        }
        return protocolAdaptor;
    }


}
