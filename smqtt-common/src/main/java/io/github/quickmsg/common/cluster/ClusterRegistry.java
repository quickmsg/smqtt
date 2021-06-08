package io.github.quickmsg.common.cluster;

import io.github.quickmsg.common.enums.ClusterEvent;
import io.github.quickmsg.common.spi.DynamicLoader;
import io.github.quickmsg.common.utils.MessageUtils;
import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttPublishVariableHeader;
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
     * @return {@link Flux<ClusterMessage>}
     */
    Flux<ClusterMessage> handlerClusterMessage();


    /**
     * 开始订阅Node事件
     *
     * @return {@link Flux<ClusterEvent>}
     */
    Flux<ClusterEvent> clusterEvent();


    /**
     * 获取集群节点信息
     *
     * @return {@link Flux<ClusterNode>}
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
    default Mono<Void> spreadPublishMessage(MqttPublishMessage message) {
        return spreadMessage(clusterMessage(message));
    }


    /**
     * 构建集群消息体
     *
     * @param message {@link MqttPublishMessage}
     * @return {@link ClusterMessage}
     */
    default ClusterMessage clusterMessage(MqttPublishMessage message) {
        MqttPublishVariableHeader header = message.variableHeader();
        MqttFixedHeader fixedHeader = message.fixedHeader();
        return ClusterMessage.builder()
                .message(MessageUtils.copyReleaseByteBuf(message.payload()))
                .topic(header.topicName())
                .retain(fixedHeader.isRetain())
                .qos(fixedHeader.qosLevel().value())
                .build();
    }


}
