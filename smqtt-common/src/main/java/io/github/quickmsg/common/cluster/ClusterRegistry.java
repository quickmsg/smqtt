package io.github.quickmsg.common.cluster;

import io.github.quickmsg.common.config.BootstrapConfig;
import io.github.quickmsg.common.enums.ClusterStatus;
import io.github.quickmsg.common.message.ClusterMessage;
import io.github.quickmsg.common.message.HeapMqttMessage;
import io.github.quickmsg.common.spi.DynamicLoader;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author luxurong
 */
public interface ClusterRegistry {


    ClusterRegistry INSTANCE = DynamicLoader.findFirst(ClusterRegistry.class).orElse(null);


    /**
     * 开始监听
     *
     * @param c 集群配置
     */
    void registry(BootstrapConfig.ClusterConfig c);


    /**
     * 开始订阅消息
     *
     * @return {@link HeapMqttMessage}
     */
    Flux<ClusterMessage> handlerClusterMessage();


    /**
     * 开始订阅Node事件
     *
     * @return {@link ClusterStatus}
     */
    Flux<ClusterStatus> clusterEvent();


    /**
     * 获取集群节点信息
     *
     * @return {@link ClusterNode}
     */
    List<ClusterNode> getClusterNode();


    /**
     * 扩散消息
     *
     * @param clusterMessage 集群消息
     * @return {@link Mono}
     */
    Mono<Void> spreadMessage(ClusterMessage clusterMessage);


    /**
     * 停止
     *
     * @return {@link Mono}
     */
    Mono<Void> shutdown();


    /**
     * 扩散消息
     *
     * @param message mqtt Publish消息
     * @return {@link Mono}
     */
    default Mono<Void> spreadPublishMessage(ClusterMessage message) {
        return spreadMessage(message);
    }


}
