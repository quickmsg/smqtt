package com.github.smqtt.common.config;

import com.github.smqtt.common.auth.PasswordAuthentication;
import com.github.smqtt.common.channel.ChannelRegistry;
import com.github.smqtt.common.message.MessageRegistry;
import com.github.smqtt.common.protocol.ProtocolAdaptor;
import com.github.smqtt.common.topic.TopicRegistry;
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
