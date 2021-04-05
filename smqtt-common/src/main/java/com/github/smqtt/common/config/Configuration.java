package com.github.smqtt.common.config;

import com.github.smqtt.common.auth.BasicAuthentication;
import com.github.smqtt.common.channel.ChannelRegistry;
import com.github.smqtt.common.message.MessageRegistry;
import com.github.smqtt.common.protocol.ProtocolAdaptor;
import com.github.smqtt.common.topic.TopicRegistry;
import reactor.netty.tcp.TcpServerConfig;

/**
 * @author luxurong
 * @date 2021/3/29 11:25
 * @description
 */
public interface Configuration {


    int getBossThreadSize();


    int getWorkThreadSize();


    int getPort();


    void loadTcpServerConfig(TcpServerConfig tcpServerConfig);


    Class<ChannelRegistry> getChannelRegistry();

    Class<MessageRegistry> getMessageRegistry();


    Class<TopicRegistry> getTopicRegistry();


    Class<ProtocolAdaptor> getProtocolAdaptor();


    BasicAuthentication getBasicAuthentication();

}
