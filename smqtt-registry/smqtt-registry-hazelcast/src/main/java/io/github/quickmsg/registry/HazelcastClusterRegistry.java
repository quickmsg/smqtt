package io.github.quickmsg.registry;

import com.hazelcast.cluster.Member;
import com.hazelcast.cluster.MembershipEvent;
import com.hazelcast.cluster.MembershipListener;
import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.TcpIpConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import io.github.quickmsg.common.bootstrap.BootstrapKey;
import io.github.quickmsg.common.cluster.ClusterConfig;
import io.github.quickmsg.common.cluster.ClusterMessage;
import io.github.quickmsg.common.cluster.ClusterNode;
import io.github.quickmsg.common.cluster.ClusterRegistry;
import io.github.quickmsg.common.enums.ClusterEvent;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * hazelcast集群注册
 *
 * @author zhaopeng
 * @date 2021/07/02
 */
@Slf4j
public class HazelcastClusterRegistry implements ClusterRegistry {

    private Sinks.Many<ClusterMessage> messageMany = Sinks.many().multicast().onBackpressureBuffer();

    private Sinks.Many<ClusterEvent> eventMany = Sinks.many().multicast().onBackpressureBuffer();

    /**
     * hazelcast实例
     */
    private HazelcastInstance hazelcastInstance;

    @Override
    public void registry(ClusterConfig clusterConfig) {
        Config cfg = new Config()
                .setInstanceName(clusterConfig.getNodeName())
                .addMapConfig(new MapConfig()
                    .setName(clusterConfig.getNodeName())
                    .setBackupCount(1)
                    .setTimeToLiveSeconds(300)
                );

        TcpIpConfig tcpIpConfig = cfg.getNetworkConfig()
                .setPort(BootstrapKey.HazelcastParameter.PORT)
                .setPortAutoIncrement(false)
                .getJoin()
                .getTcpIpConfig()
                .setEnabled(false);

        // 循环注册集群IP地址
        Arrays.stream(clusterConfig.getClusterUrl().split(BootstrapKey.SplitSymbol.COMMA)).forEach(record->{
                String ip = record.split(BootstrapKey.SplitSymbol.COLON)[0];
                tcpIpConfig.addMember(ip);
        });

        // 初始化hazelcast实例
        this.hazelcastInstance = Hazelcast.newHazelcastInstance(cfg);
        // 注册监听事件
        this.hazelcastInstance.getCluster().addMembershipListener(new ClusterHandler());
    }

    @Override
    public Flux<ClusterMessage> handlerClusterMessage() {
        return messageMany.asFlux();
    }

    @Override
    public List<ClusterNode> getClusterNode() {
        Set<HazelcastInstance> hazelcastInstanceSet = Hazelcast.getAllHazelcastInstances();
        return hazelcastInstanceSet.stream().map(this::clusterNode).collect(Collectors.toList());
    }

    private ClusterNode clusterNode(HazelcastInstance hazelcastInstance) {
        // TODO host namespace
        return ClusterNode.builder()
                .alias(hazelcastInstance.getName())
                .host(null)
                .port(BootstrapKey.HazelcastParameter.PORT)
                .namespace(null)
                .build();
    }

    @Override
    public Mono<Void> spreadMessage(ClusterMessage clusterMessage) {
        log.info("cluster send message {} ", clusterMessage);
        hazelcastInstance.getMap("customers").put(1, clusterMessage);
        return Mono.empty();
    }

    @Override
    public Mono<Void> shutdown() {
        return Mono.fromRunnable(() -> Optional.ofNullable(hazelcastInstance)
                .ifPresent(HazelcastInstance::shutdown));
    }

    @Override
    public Flux<ClusterEvent> clusterEvent() {
        return eventMany.asFlux();
    }

    class ClusterHandler implements MembershipListener {

        @Override
        public void memberAdded(MembershipEvent membershipEvent) {
            eventMany.tryEmitNext(ClusterEvent.ADDED);
        }

        @Override
        public void memberRemoved(MembershipEvent membershipEvent) {
            eventMany.tryEmitNext(ClusterEvent.REMOVED);
        }
    }
}
