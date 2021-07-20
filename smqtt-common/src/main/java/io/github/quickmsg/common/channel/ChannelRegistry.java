package io.github.quickmsg.common.channel;

import io.github.quickmsg.common.StartUp;
import io.github.quickmsg.common.cluster.ClusterMessage;
import io.github.quickmsg.common.spi.DynamicLoader;

import java.util.Collection;

/**
 * @author luxurong
 */
public interface ChannelRegistry extends StartUp {


    ChannelRegistry INSTANCE = DynamicLoader.findFirst(ChannelRegistry.class).orElse(null);


    /**
     * 关闭通道
     *
     * @param mqttChannel {@link MqttChannel}
     */
    void close(MqttChannel mqttChannel);

    /**
     * 注册通道
     *
     * @param clientIdentifier 客户端id
     * @param mqttChannel      {@link MqttChannel}
     */
    void registry(String clientIdentifier, MqttChannel mqttChannel);

    /**
     * 判读通道是否存在
     *
     * @param clientIdentifier 客户端id
     * @return 布尔
     */
    boolean exists(String clientIdentifier);


    /**
     * 获取通道
     *
     * @param clientIdentifier 客户端id
     * @return MqttChannel
     */
    MqttChannel get(String clientIdentifier);


    /**
     * 获取通道计数
     *
     * @return 通道数
     */
    Integer counts();


    /**
     * 获取说有channel信息
     *
     * @return {@link Collection}
     */
    Collection<MqttChannel> getChannels();

}
