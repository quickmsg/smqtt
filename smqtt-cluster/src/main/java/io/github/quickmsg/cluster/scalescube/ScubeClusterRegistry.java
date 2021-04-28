package io.github.quickmsg.cluster.scalescube;

import io.github.quickmsg.common.cluster.ClusterEvent;
import io.github.quickmsg.common.cluster.ClusterMessage;
import io.github.quickmsg.common.cluster.ClusterRegistry;
import io.scalecube.cluster.ClusterImpl;
import io.scalecube.cluster.ClusterMessageHandler;
import io.scalecube.cluster.membership.MembershipEvent;
import io.scalecube.cluster.transport.api.Message;
import io.scalecube.net.Address;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.stream.Collectors;

/**
 * @author luxurong
 */
public class ScubeClusterRegistry implements ClusterRegistry<ScubeClusterConfig> {

    private Sinks.Many<ClusterMessage> messageMany = Sinks.many().multicast().onBackpressureBuffer();

    private Sinks.Many<ClusterEvent<MembershipEvent, ScubeClusterNode>> eventMany = Sinks.many().multicast().onBackpressureBuffer();


    @Override
    public Mono<Void> registry(ScubeClusterConfig scubeClusterConfig) {
        return Mono.fromRunnable(() -> {
            new ClusterImpl()
                    .config(opts -> opts.memberAlias(scubeClusterConfig.getNodeName()))
                    .membership(opts -> opts.seedMembers(scubeClusterConfig
                            .getClusterUrl()
                            .stream()
                            .map(Address::from)
                            .collect(Collectors.toList())))
                    .handler(cluster -> new ClusterHandler())
                    .startAwait();
        });
    }

    @Override
    public Flux<ClusterMessage> clusterMessage() {
        return messageMany.asFlux();
    }

    @Override
    public Flux<ClusterEvent<MembershipEvent, ScubeClusterNode>> clusterEvent() {
        return eventMany.asFlux();
    }


    class ClusterHandler implements ClusterMessageHandler {

        @Override
        public void onMessage(Message message) {
            messageMany.tryEmitNext(new ClusterMessage());
        }

        @Override
        public void onGossip(Message gossip) {
            messageMany.tryEmitNext(new ClusterMessage());

        }

        @Override
        public void onMembershipEvent(MembershipEvent event) {
            eventMany.tryEmitNext(new ClusterEvent(event, new ScubeClusterNode(event.member())));
        }
    }
}
