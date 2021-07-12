package io.github.quickmsg.common.cluster;

import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author luxurong
 */
public interface ClusterHandler<T> {


    /**
     * 获取集群信息
     *
     * @return {@link List<ClusterNode>}
     */
    List<ClusterNode> getClusterInfo();


    /**
     * 传播集群消息
     *
     * @param clusterMessage 集群
     * @return {@link Mono}
     */
    Mono<Void> spreadGossip(ClusterMessage clusterMessage);


    /**
     * 传播集群消息
     *
     * @param clusterMessage 集群
     * @return {@link Mono}
     */
    Mono<Void> send(ClusterMessage clusterMessage);


}
