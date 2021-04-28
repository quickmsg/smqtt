package io.github.quickmsg.common.config;

import io.github.quickmsg.common.auth.PasswordAuthentication;
import io.github.quickmsg.common.channel.ChannelRegistry;
import io.github.quickmsg.common.message.MessageRegistry;
import io.github.quickmsg.common.protocol.ProtocolAdaptor;
import io.github.quickmsg.common.topic.TopicRegistry;
import reactor.netty.tcp.TcpServerConfig;

import java.util.function.Consumer;

/**
 * @author luxurong
 * @date 2021/4/19 17:12
 * @description
 */
public interface AbstractConfiguration extends Configuration {


    Integer getWebSocketPort();


    Class<? extends ChannelRegistry> getChannelRegistry();


    Class<? extends MessageRegistry> getMessageRegistry();


    Class<? extends TopicRegistry> getTopicRegistry();


    Class<? extends ProtocolAdaptor> getProtocolAdaptor();


    Consumer<? super TcpServerConfig> getTcpServerConfig();


    Class<? extends PasswordAuthentication> getPasswordAuthentication();

    PasswordAuthentication getReactivePasswordAuth();

}
