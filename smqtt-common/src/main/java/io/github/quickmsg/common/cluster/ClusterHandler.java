package io.github.quickmsg.common.cluster;

import io.github.quickmsg.common.message.HeapMqttMessage;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author luxurong
 */
public interface ClusterHandler<T> {


    /**
     * 获取集群信息
     *
     * @return {@link ClusterNode}
     */
    List<ClusterNode> getClusterInfo();


    /**
     * 传播集群消息
     *
     * @param heapMqttMessage 集群
     * @return {@link Mono}
     */
    Mono<Void> spreadGossip(HeapMqttMessage heapMqttMessage);


    /**
     * 传播集群消息
     *
     * @param heapMqttMessage 集群
     * @return {@link Mono}
     */
    Mono<Void> send(HeapMqttMessage heapMqttMessage);


}
