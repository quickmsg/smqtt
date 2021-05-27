package io.github.quickmsg.common.interceptor;

import io.github.quickmsg.common.annotation.Intercept;
import io.github.quickmsg.common.protocol.ProtocolAdaptor;

import java.lang.reflect.Proxy;

/**
 * @author luxurong
 */
public interface Interceptor {

    /**
     * 拦截目标参数
     *
     * @param invocation 消息体
     * @return Object 返回值
     */
    Object intercept(Invocation invocation);


    /**
     * 拦截链
     *
     * @param protocolAdaptor 协议转换
     * @param interceptor     拦截器
     * @return 代理类
     */
    default ProtocolAdaptor proxyProtocol(ProtocolAdaptor protocolAdaptor, Interceptor interceptor) {
        return (ProtocolAdaptor) Proxy.newProxyInstance(protocolAdaptor.getClass().getClassLoader(), new Class[]{ProtocolAdaptor.class}, new InterceptorHandler(interceptor,protocolAdaptor));
    }


    /**
     * 排序
     * @return 排序
     */
    int sort();

}
