package com.github.smqtt.core.mqtt;

import com.github.smqtt.common.channel.ChannelRegistry;
import com.github.smqtt.common.config.Configuration;
import com.github.smqtt.common.protocol.ProtocolAdaptor;
import com.github.smqtt.common.topic.TopicRegistry;
import lombok.Getter;
import reactor.netty.tcp.TcpServerConfig;

/**
 * @author luxurong
 * @date 2021/3/30 13:26
 * @Description MQTT协议配置类
 */
@Getter
public class MqttConfiguration implements Configuration {


    private Class<ChannelRegistry> channelRegistry;


    private Class<TopicRegistry> topicRegistry;

    private Class<ProtocolAdaptor> ProtocolAdaptor;


    private MqttReceiveContext mqttReceiveContext;


    @Override
    public int getBossThreadSize() {
        return 0;
    }

    @Override
    public int getWorkThreadSize() {
        return 0;
    }

    @Override
    public int getPort() {
        return 0;
    }

    @Override
    public void loadTcpServerConfig(TcpServerConfig tcpServerConfig) {
        //加载netty options配置


    }

}
