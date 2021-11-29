package io.github.quickmsg.registry;

import io.github.quickmsg.common.cluster.ClusterNode;
import io.github.quickmsg.common.cluster.ClusterRegistry;
import io.github.quickmsg.common.config.BootstrapConfig;
import io.github.quickmsg.common.enums.ClusterStatus;
import io.github.quickmsg.common.message.HeapMqttMessage;
import io.scalecube.cluster.Cluster;
import io.scalecube.cluster.ClusterImpl;
import io.scalecube.cluster.ClusterMessageHandler;
import io.scalecube.cluster.Member;
import io.scalecube.cluster.membership.MembershipEvent;
import io.scalecube.cluster.transport.api.Message;
import io.scalecube.net.Address;
import io.scalecube.reactor.RetryNonSerializedEmitFailureHandler;
import io.scalecube.transport.netty.tcp.TcpTransportFactory;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author luxurong
 */
@Slf4j
public class ScubeClusterRegistry implements ClusterRegistry {

    private Sinks.Many<HeapMqttMessage> messageMany = Sinks.many().multicast().onBackpressureBuffer();

    private Sinks.Many<ClusterStatus> eventMany = Sinks.many().multicast().onBackpressureBuffer();

    private Cluster cluster;


    @Override
    public void registry(BootstrapConfig.ClusterConfig clusterConfig) {
        this.cluster = new ClusterImpl()
                .config(opts ->
                        opts.memberAlias(clusterConfig.getNode())
                                .externalHost(Optional.ofNullable(clusterConfig.getExternal())
                                        .map(BootstrapConfig.ClusterExternal::getHost)
                                        .orElse(null))
                                .externalPort(Optional.ofNullable(clusterConfig.getExternal())
                                        .map(BootstrapConfig.ClusterExternal::getPort)
                                        .orElse(null))
                )
                .transportFactory(TcpTransportFactory::new)
                .transport(transportConfig -> transportConfig.port(clusterConfig.getPort()))
                .membership(opts -> opts.seedMembers(Arrays.stream(clusterConfig
                        .getUrl()
                        .split(","))
                        .map(Address::from)
                        .collect(Collectors.toList())).namespace(clusterConfig.getNamespace()))
                .handler(cluster -> new ClusterHandler())
                .startAwait();
    }

    @Override
    public Flux<HeapMqttMessage> handlerClusterMessage() {
        return messageMany.asFlux();
    }

    @Override
    public List<ClusterNode> getClusterNode() {
        return Optional.ofNullable(cluster)
                .map(cs -> cs.members().stream().map(this::clusterNode).collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    private ClusterNode clusterNode(Member member) {
        return ClusterNode.builder()
                .alias(member.alias())
                .host(member.address().host())
                .port(member.address().port())
                .namespace(member.namespace())
                .build();
    }

    @Override
    public Mono<Void> spreadMessage(HeapMqttMessage heapMqttMessage) {
        log.info("cluster send message {} ", heapMqttMessage);
        return Mono.when(
                cluster.otherMembers()
                        .stream()
                        .map(member ->
                                Optional.ofNullable(cluster)
                                        .map(cs ->
                                                cs.send(member, Message.withData(heapMqttMessage).build()).then()
                                        ).orElse(Mono.empty()))
                        .collect(Collectors.toList()));
    }

    @Override
    public Mono<Void> shutdown() {
        return Mono.fromRunnable(() -> Optional.ofNullable(cluster)
                .ifPresent(Cluster::shutdown));
    }

    @Override
    public Flux<ClusterStatus> clusterEvent() {
        return eventMany.asFlux();
    }


    class ClusterHandler implements ClusterMessageHandler {

        @Override
        public void onMessage(Message message) {
            log.info("cluster accept message {} ", message);
            messageMany.emitNext(message.data(),new RetryNonSerializedEmitFailureHandler());
        }

        @Override
        public void onGossip(Message message) {
            log.info("cluster accept message {} ", message);
            messageMany.emitNext(message.data(),new RetryNonSerializedEmitFailureHandler());
        }

        @Override
        public void onMembershipEvent(MembershipEvent event) {
            Member member = event.member();
            log.info("cluster onMembershipEvent {}  {}", member, event);
            switch (event.type()) {
                case ADDED:
                    eventMany.tryEmitNext(ClusterStatus.ADDED);
                    break;
                case LEAVING:
                    eventMany.tryEmitNext(ClusterStatus.LEAVING);
                    break;
                case REMOVED:
                    eventMany.tryEmitNext(ClusterStatus.REMOVED);
                    break;
                case UPDATED:
                    eventMany.tryEmitNext(ClusterStatus.UPDATED);
                    break;
                default:
                    break;
            }
        }
    }
}
