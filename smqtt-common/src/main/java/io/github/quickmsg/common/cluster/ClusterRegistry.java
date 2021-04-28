package io.github.quickmsg.common.cluster;

import io.github.quickmsg.common.spi.DynamicLoader;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author luxurong
 * @date 2021/4/9 18:01
 * @description
 */
public interface ClusterRegistry<C extends ClusterConfig> {


    ClusterRegistry INSTANCE = DynamicLoader.findFirst(ClusterRegistry.class).orElse(null);


    /**
     * 开始监听
     *
     * @param c 集群配置
     * @return Mono
     */
    Mono<Void> registry(C c);


    /**
     * 开始订阅消息
     *
     * @return Flux
     */
    Flux<ClusterMessage> clusterMessage();


    /**
     * 开始订阅Node事件
     *
     * @return Flux
     */
    <E, N extends ClusterNode> Flux<ClusterEvent<E, N>> clusterEvent();


}
