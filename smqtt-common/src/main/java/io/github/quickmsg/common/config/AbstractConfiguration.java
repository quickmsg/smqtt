package io.github.quickmsg.common.config;

import io.github.quickmsg.common.auth.PasswordAuthentication;
import reactor.netty.tcp.TcpServerConfig;

import java.util.function.Consumer;

/**
 * @author luxurong
 */
public interface AbstractConfiguration extends Configuration {


    Integer getWebSocketPort();


    Consumer<? super TcpServerConfig> getTcpServerConfig();


    PasswordAuthentication getReactivePasswordAuth();


}
