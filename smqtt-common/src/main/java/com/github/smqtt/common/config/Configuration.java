package com.github.smqtt.common.config;

import com.github.smqtt.common.auth.PasswordAuthentication;
import com.github.smqtt.common.channel.ChannelRegistry;
import com.github.smqtt.common.message.MessageRegistry;
import com.github.smqtt.common.protocol.ProtocolAdaptor;
import com.github.smqtt.common.topic.TopicRegistry;
import reactor.netty.tcp.TcpServerConfig;

import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * @author luxurong
 * @date 2021/3/29 11:25
 * @description
 */
public interface Configuration {


    Integer getBossThreadSize();


    Integer getWorkThreadSize();


    Integer getPort();

    Integer getWebSocketPort();


    Class<? extends ChannelRegistry> getChannelRegistry();


    Class<? extends MessageRegistry> getMessageRegistry();


    Class<? extends TopicRegistry> getTopicRegistry();


    Class<? extends ProtocolAdaptor> getProtocolAdaptor();


    Consumer<? super TcpServerConfig> getTcpServerConfig();


    Class<? extends PasswordAuthentication> getPasswordAuthentication();


    Integer getLowWaterMark();

    Integer getHighWaterMark();

    Boolean getWiretap();


    PasswordAuthentication getReactivePasswordAuth();


}
