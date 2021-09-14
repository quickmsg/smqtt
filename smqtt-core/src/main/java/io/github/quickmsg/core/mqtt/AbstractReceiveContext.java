package io.github.quickmsg.core.mqtt;

import io.github.quickmsg.common.auth.PasswordAuthentication;
import io.github.quickmsg.common.channel.ChannelRegistry;
import io.github.quickmsg.common.cluster.ClusterRegistry;
import io.github.quickmsg.common.config.AbstractConfiguration;
import io.github.quickmsg.common.config.Configuration;
import io.github.quickmsg.common.context.ReceiveContext;
import io.github.quickmsg.common.message.MessageRegistry;
import io.github.quickmsg.common.message.RecipientRegistry;
import io.github.quickmsg.common.protocol.ProtocolAdaptor;
import io.github.quickmsg.common.rule.DslExecutor;
import io.github.quickmsg.common.topic.TopicRegistry;
import io.github.quickmsg.common.transport.Transport;
import io.github.quickmsg.core.cluster.InJvmClusterRegistry;
import io.github.quickmsg.core.spi.*;
import io.github.quickmsg.dsl.RuleDslParser;
import io.github.quickmsg.source.SourceManager;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.scheduler.Schedulers;
import reactor.netty.resources.LoopResources;

import java.util.Optional;

/**
 * @author luxurong
 */
@Getter
@Setter
@Slf4j
public abstract class AbstractReceiveContext<T extends Configuration> implements ReceiveContext<T> {

    private T configuration;

    private LoopResources loopResources;

    private Transport<T> transport;

    private final ProtocolAdaptor protocolAdaptor;

    private final ChannelRegistry channelRegistry;

    private final TopicRegistry topicRegistry;

    private final MessageRegistry messageRegistry;

    private final PasswordAuthentication passwordAuthentication;

    private final ClusterRegistry clusterRegistry;

    private final RecipientRegistry recipientRegistry;

    private final DslExecutor dslExecutor;


    public AbstractReceiveContext(T configuration, Transport<T> transport) {
        AbstractConfiguration abstractConfiguration = castConfiguration(configuration);
        RuleDslParser ruleDslParser = new RuleDslParser(abstractConfiguration.getRuleDefinitions());

        this.configuration = configuration;
        this.transport = transport;
        this.dslExecutor = ruleDslParser.parseRule();
        this.recipientRegistry = recipientRegistry();
        this.protocolAdaptor = protocolAdaptor();
        this.channelRegistry = channelRegistry();
        this.topicRegistry = topicRegistry();
        this.loopResources = LoopResources.create("smqtt-cluster-io", configuration.getBossThreadSize(), configuration.getWorkThreadSize(), true);
        this.messageRegistry = messageRegistry();
        this.clusterRegistry = clusterRegistry();
        this.passwordAuthentication = basicAuthentication();
        this.channelRegistry.startUp(abstractConfiguration.getEnvironmentMap());
        this.messageRegistry.startUp(abstractConfiguration.getEnvironmentMap());

        abstractConfiguration.getSourceDefinitions().forEach(SourceManager::loadSource);
    }


    private RecipientRegistry recipientRegistry() {
        return Optional.ofNullable(RecipientRegistry.INSTANCE)
                .orElse(new EmptyRecipientRegistry());
    }

    private MessageRegistry messageRegistry() {
        return Optional.ofNullable(MessageRegistry.INSTANCE)
                .orElse(new DefaultMessageRegistry());
    }

    private PasswordAuthentication basicAuthentication() {
        AbstractConfiguration abstractConfiguration = castConfiguration(configuration);
        return Optional.ofNullable(PasswordAuthentication.INSTANCE)
                .orElse(abstractConfiguration.getReactivePasswordAuth());
    }

    private ChannelRegistry channelRegistry() {
        return Optional.ofNullable(ChannelRegistry.INSTANCE)
                .orElse(new DefaultChannelRegistry());
    }

    private TopicRegistry topicRegistry() {
        return Optional.ofNullable(TopicRegistry.INSTANCE)
                .orElse(new DefaultTopicRegistry());
    }

    private ProtocolAdaptor protocolAdaptor() {
        return Optional.ofNullable(ProtocolAdaptor.INSTANCE)
                .orElse(new DefaultProtocolAdaptor(Schedulers.newBoundedElastic(configuration.getBusinessThreadSize(), configuration.getBusinessQueueSize(), "business-io")))
                .proxy();
    }

    private ClusterRegistry clusterRegistry() {
        return Optional.ofNullable(ClusterRegistry.INSTANCE)
                .orElse(new InJvmClusterRegistry());
    }


    private AbstractConfiguration castConfiguration(T configuration) {
        return (AbstractConfiguration) configuration;
    }

}
