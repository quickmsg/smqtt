package com.github.smqtt.cluster.scalescube;

import com.github.smqtt.cluster.ClusterRegistry;
import io.scalecube.cluster.Cluster;
import io.scalecube.cluster.ClusterImpl;
import io.scalecube.cluster.ClusterMessageHandler;
import io.scalecube.cluster.membership.MembershipEvent;
import io.scalecube.cluster.transport.api.Message;
import io.scalecube.net.Address;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

/**
 * @author luxurong
 * @date 2021/4/9 19:31
 * @description
 */
public class ScubeClusterRegistry implements ClusterRegistry<ScubeClusterConfig> {



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
//                    .handler(_cluster->)
                    .startAwait();
        });
    }

    @Override
    public Flux<String> subscribe() {
        return null;
    }

    @Override
    public Flux<String> clusterEvent() {
        return null;
    }


    static class ClusterHandler implements ClusterMessageHandler{

        @Override
        public void onMessage(Message message) {

        }

        @Override
        public void onGossip(Message gossip) {

        }

        @Override
        public void onMembershipEvent(MembershipEvent event) {

        }
    }
}
