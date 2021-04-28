package io.github.quickmsg.core.mqtt;

import io.github.quickmsg.core.DefaultTopicRegistry;
import io.github.quickmsg.common.auth.PasswordAuthentication;
import io.github.quickmsg.common.channel.ChannelRegistry;
import io.github.quickmsg.common.config.AbstractConfiguration;
import io.github.quickmsg.common.config.Configuration;
import io.github.quickmsg.common.context.ReceiveContext;
import io.github.quickmsg.common.message.MessageRegistry;
import io.github.quickmsg.common.protocol.ProtocolAdaptor;
import io.github.quickmsg.common.spi.DynamicLoader;
import io.github.quickmsg.common.topic.TopicRegistry;
import io.github.quickmsg.common.transport.Transport;
import io.github.quickmsg.core.DefaultChannelRegistry;
import io.github.quickmsg.core.DefaultMessageRegistry;
import io.github.quickmsg.core.DefaultProtocolAdaptor;
import lombok.Getter;
import lombok.Setter;
import reactor.netty.resources.LoopResources;

import java.util.Optional;

/**
 * @author luxurong
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
        AbstractConfiguration abstractConfiguration = castConfiguration(configuration);
        return Optional.ofNullable(DynamicLoader
                .findFirst(abstractConfiguration.getMessageRegistry())
                .map(messageRegistry -> (MessageRegistry) messageRegistry)
                .orElse(MessageRegistry.INSTANCE)).orElse(new DefaultMessageRegistry());
    }

    private PasswordAuthentication basicAuthentication() {
        AbstractConfiguration abstractConfiguration = castConfiguration(configuration);
        return Optional.ofNullable(DynamicLoader
                .findFirst(abstractConfiguration.getPasswordAuthentication())
                .map(passwordAuthentication -> (PasswordAuthentication) passwordAuthentication)
                .orElse(PasswordAuthentication.INSTANCE)).orElse(abstractConfiguration.getReactivePasswordAuth());
    }

    ;

    private ChannelRegistry channelRegistry(T configuration) {
        AbstractConfiguration abstractConfiguration = castConfiguration(configuration);
        return Optional.ofNullable(DynamicLoader
                .findFirst(abstractConfiguration.getChannelRegistry())
                .map(channelRegistry -> (ChannelRegistry) channelRegistry)
                .orElse(ChannelRegistry.INSTANCE)).orElse(new DefaultChannelRegistry());
    }

    private TopicRegistry topicRegistry(T configuration) {
        AbstractConfiguration abstractConfiguration = castConfiguration(configuration);
        return Optional.ofNullable(DynamicLoader
                .findFirst(abstractConfiguration.getTopicRegistry())
                .map(topicRegistry -> (TopicRegistry) topicRegistry)
                .orElse(TopicRegistry.INSTANCE)).orElse(new DefaultTopicRegistry());
    }

    private ProtocolAdaptor protocolAdaptor(T configuration) {
        AbstractConfiguration abstractConfiguration = castConfiguration(configuration);
        return Optional.ofNullable(DynamicLoader
                .findFirst(abstractConfiguration.getProtocolAdaptor())
                .map(ProtocolAdaptor::proxy)
                .orElse(ProtocolAdaptor.INSTANCE)).orElse(new DefaultProtocolAdaptor().proxy());

    }

    private AbstractConfiguration castConfiguration(T configuration) {
        return (AbstractConfiguration) configuration;
    }


}
