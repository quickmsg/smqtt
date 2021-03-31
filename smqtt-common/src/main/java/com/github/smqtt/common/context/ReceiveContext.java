package com.github.smqtt.common.context;

import com.github.smqtt.common.channel.ChannelRegistry;
import com.github.smqtt.common.channel.MqttChannel;
import com.github.smqtt.common.config.Configuration;
import com.github.smqtt.common.protocol.ProtocolAdaptor;
import com.github.smqtt.common.transport.Transport;
import io.netty.handler.codec.mqtt.MqttMessage;
import lombok.Getter;
import lombok.Setter;
import reactor.netty.resources.LoopResources;

import java.util.function.BiConsumer;

/**
 * @author luxurong
 * @date 2021/3/29 20:29
 * @description
 */
@Getter
@Setter
public abstract class ReceiveContext<T extends Configuration> implements BiConsumer<MqttChannel, MqttMessage> {

    private T configuration;

    private LoopResources loopResources;

    private Transport<T> transport;

    private final ProtocolAdaptor<T> protocolAdaptor;

    private final ChannelRegistry channelRegistry;

    public ReceiveContext(T configuration, Transport<T> transport) {
        this.configuration = configuration;
        this.transport = transport;
        this.protocolAdaptor = protocolAdaptor(configuration);
        this.channelRegistry = channelRegistry(configuration);
        this.loopResources = LoopResources.create("smqtt-cluster-io", configuration.getBossThreadSize(), configuration.getWorkThreadSize(), true);
    }


    public abstract ChannelRegistry channelRegistry(T configuration);

    public abstract ProtocolAdaptor<T> protocolAdaptor(T configuration);


}
