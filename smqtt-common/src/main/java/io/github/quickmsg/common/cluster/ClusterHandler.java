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
     * @return List
     */
    List<ClusterNode> getClusterInfo();


    /**
     * 传播集群消息
     *
     * @param clusterMessage 集群
     * @return List
     */
    Mono<Void> spreadGossip(ClusterMessage clusterMessage);



    /**
     * 传播集群消息
     *
     * @param clusterMessage 集群
     * @return List
     */
    Mono<Void> send(ClusterMessage clusterMessage);


}
