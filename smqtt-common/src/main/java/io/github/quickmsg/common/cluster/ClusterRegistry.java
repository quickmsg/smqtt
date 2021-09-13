package io.github.quickmsg.common.cluster;

import io.github.quickmsg.common.enums.ClusterStatus;
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
    void registry(ClusterConfig c);


    /**
     * 开始订阅消息
     *
     * @return {@link HeapMqttMessage}
     */
    Flux<HeapMqttMessage> handlerClusterMessage();


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
     * @param heapMqttMessage 集群消息
     * @return {@link Mono}
     */
    Mono<Void> spreadMessage(HeapMqttMessage heapMqttMessage);


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
    default Mono<Void> spreadPublishMessage(HeapMqttMessage message) {
        return spreadMessage(message);
    }


}
