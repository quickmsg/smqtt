package io.github.quickmsg.common.config;

import io.github.quickmsg.common.auth.PasswordAuthentication;
import io.github.quickmsg.common.environment.EnvContext;
import reactor.netty.tcp.TcpServerConfig;

import java.util.function.Consumer;

/**
 * @author luxurong
 */
public interface AbstractConfiguration extends Configuration {


    /**
     * 获取websocket端口
     *
     * @return port
     */
    Integer getWebSocketPort();


    /**
     * 获取Tcp服务配置
     *
     * @return {@link Consumer<? super TcpServerConfig> }
     */
    Consumer<? super TcpServerConfig> getTcpServerConfig();


    /**
     * 获取服务端认证
     *
     * @return {@link PasswordAuthentication}
     */
    PasswordAuthentication getReactivePasswordAuth();


    /**
     * 获取环境变量
     *
     * @return {@link EnvContext}
     */
    EnvContext getEnvContext();


}
