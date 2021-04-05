package com.github.smqtt.core.mqtt;

import com.github.smqtt.common.auth.BasicAuthentication;
import com.github.smqtt.common.channel.ChannelRegistry;
import com.github.smqtt.common.config.Configuration;
import com.github.smqtt.common.message.MessageRegistry;
import com.github.smqtt.common.protocol.ProtocolAdaptor;
import com.github.smqtt.common.topic.TopicRegistry;
import lombok.Data;
import reactor.netty.tcp.TcpServerConfig;

/**
 * @author luxurong
 * @date 2021/3/30 13:26
 * @Description MQTT协议配置类
 */
@Data
public class MqttConfiguration implements Configuration {

    private int port;

    private BasicAuthentication basicAuthentication;


    private Class<ChannelRegistry> channelRegistry = ChannelRegistry.class;


    private Class<TopicRegistry> topicRegistry = TopicRegistry.class;

    private Class<ProtocolAdaptor> protocolAdaptor = ProtocolAdaptor.class;

    private Class<MessageRegistry> messageRegistry = MessageRegistry.class;



    private MqttReceiveContext mqttReceiveContext;


    @Override
    public int getBossThreadSize() {
        return 1;
    }

    @Override
    public int getWorkThreadSize() {
        return 4;
    }


    @Override
    public void loadTcpServerConfig(TcpServerConfig tcpServerConfig) {
        //加载netty options配置


    }

}
