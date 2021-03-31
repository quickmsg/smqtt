package com.github.smqtt.common.config;

import com.github.smqtt.common.channel.ChannelRegistry;
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

}
