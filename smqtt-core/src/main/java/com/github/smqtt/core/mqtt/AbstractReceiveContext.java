package com.github.smqtt.core.mqtt;

import com.github.smqtt.common.auth.PasswordAuthentication;
import com.github.smqtt.common.channel.ChannelRegistry;
import com.github.smqtt.common.config.Configuration;
import com.github.smqtt.common.context.ReceiveContext;
import com.github.smqtt.common.message.MessageRegistry;
import com.github.smqtt.common.protocol.ProtocolAdaptor;
import com.github.smqtt.common.spi.DynamicLoader;
import com.github.smqtt.common.topic.TopicRegistry;
import com.github.smqtt.common.transport.Transport;
import com.github.smqtt.core.DefaultChannelRegistry;
import com.github.smqtt.core.DefaultMessageRegistry;
import com.github.smqtt.core.DefaultProtocolAdaptor;
import com.github.smqtt.core.DefaultTopicRegistry;
import lombok.Getter;
import lombok.Setter;
import reactor.netty.resources.LoopResources;

import java.util.Optional;

/**
 * @author luxurong
 * @date 2021/3/29 20:29
 * @description
 */
@Getter
@Setter
public abstract class AbstractReceiveContext<T extends Configuration> implements ReceiveContext<T> {

    private T configuration;

    private LoopResources loopResources;

    private Transport<T> transport;

    private final ProtocolAdaptor protocolAdaptor;

    private final ChannelRegistry channelRegistry;

    private final TopicRegistry topicRegistry;

    private final MessageRegistry messageRegistry;

    private final PasswordAuthentication passwordAuthentication;

    public AbstractReceiveContext(T configuration, Transport<T> transport) {
        this.configuration = configuration;
        this.transport = transport;
        this.protocolAdaptor = protocolAdaptor(configuration);
        this.channelRegistry = channelRegistry(configuration);
        this.topicRegistry = topicRegistry(configuration);
        this.loopResources = LoopResources.create("smqtt-cluster-io", configuration.getBossThreadSize(), configuration.getWorkThreadSize(), true);
        this.messageRegistry = messageRegistry(configuration);
        this.passwordAuthentication = basicAuthentication();
    }

    private MessageRegistry messageRegistry(T configuration) {
        return Optional.ofNullable(DynamicLoader
                .findFirst(configuration.getMessageRegistry())
                .map(messageRegistry -> (MessageRegistry) messageRegistry)
                .orElse(MessageRegistry.INSTANCE)).orElse(new DefaultMessageRegistry());
    }

    private PasswordAuthentication basicAuthentication() {
        return Optional.ofNullable(DynamicLoader
                .findFirst(configuration.getPasswordAuthentication())
                .map(passwordAuthentication -> (PasswordAuthentication) passwordAuthentication)
                .orElse(PasswordAuthentication.INSTANCE)).orElse(configuration.getReactivePasswordAuth());
    }

    ;

    private ChannelRegistry channelRegistry(T configuration) {
        return Optional.ofNullable(DynamicLoader
                .findFirst(configuration.getChannelRegistry())
                .map(channelRegistry -> (ChannelRegistry) channelRegistry)
                .orElse(ChannelRegistry.INSTANCE)).orElse(new DefaultChannelRegistry());
    }

    private TopicRegistry topicRegistry(T configuration) {
        return Optional.ofNullable(DynamicLoader
                .findFirst(configuration.getTopicRegistry())
                .map(topicRegistry -> (TopicRegistry) topicRegistry)
                .orElse(TopicRegistry.INSTANCE)).orElse(new DefaultTopicRegistry());
    }

    private ProtocolAdaptor protocolAdaptor(T configuration) {
        return Optional.ofNullable(DynamicLoader
                .findFirst(configuration.getProtocolAdaptor())
                .map(ProtocolAdaptor::proxy)
                .orElse(ProtocolAdaptor.INSTANCE)).orElse(new DefaultProtocolAdaptor().proxy());

    }


}
