package io.github.quickmsg.common.cluster;

import io.github.quickmsg.common.enums.ClusterEvent;
import io.github.quickmsg.common.spi.DynamicLoader;
import io.netty.buffer.ByteBuf;
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
     * @return Flux
     */
    Flux<ClusterMessage> handlerClusterMessage();


    /**
     * 开始订阅Node事件
     *
     * @return Flux
     */
    Flux<ClusterEvent> clusterEvent();


    /**
     * 获取集群节点信息
     *
     * @return 节点集合
     */
    List<ClusterNode> getClusterNode();


    /**
     * 扩散消息
     *
     * @param clusterMessage 集群消息
     * @return Flux
     */
    Mono<Void> spreadMessage(ClusterMessage clusterMessage);


    /**
     * 停止
     *
     * @return Mono
     */
    Mono<Void> shutdown();


    /**
     * 扩散消息
     *
     * @param message mqtt Publish消息
     * @return Flux
     */
    default Mono<Void> spreadPublishMessage(MqttPublishMessage message) {
        return spreadMessage(clusterMessage(message));
    }


    /**
     * 构建集群消息体
     *
     * @param message mqtt Publish消息
     * @return 集群消息
     */
    default ClusterMessage clusterMessage(MqttPublishMessage message) {
        MqttPublishVariableHeader header = message.variableHeader();
        MqttFixedHeader fixedHeader = message.fixedHeader();
        return ClusterMessage.builder()
                .message(copyByteBuf(message.payload()))
                .topic(header.topicName())
                .retain(fixedHeader.isRetain())
                .qos(fixedHeader.qosLevel().value())
                .build();
    }


    /**
     * 获取消息字节数组
     *
     * @param byteBuf 消息ByteBuf
     * @return 字节数组
     */
    default byte[] copyByteBuf(ByteBuf byteBuf) {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        byteBuf.resetReaderIndex();
        return bytes;
    }


}
