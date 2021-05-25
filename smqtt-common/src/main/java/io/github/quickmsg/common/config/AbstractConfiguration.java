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
     * @return 端口
     */
    Integer getWebSocketPort();


    /**
     * 获取Tcp服务配置
     *
     * @return 配置
     */
    Consumer<? super TcpServerConfig> getTcpServerConfig();


    /**
     * 获取服务端认证
     *
     * @return 认证器
     */
    PasswordAuthentication getReactivePasswordAuth();


    /**
     * 获取环境变量
     *
     * @return 配置config
     */
    EnvContext getEnvContext();


}
