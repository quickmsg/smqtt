package io.github.quickmsg.common.context;

import io.github.quickmsg.common.ack.TimeAckManager;
import io.github.quickmsg.common.acl.AclManager;
import io.github.quickmsg.common.channel.ChannelRegistry;
import io.github.quickmsg.common.channel.MqttChannel;
import io.github.quickmsg.common.channel.traffic.TrafficHandlerLoader;
import io.github.quickmsg.common.cluster.ClusterRegistry;
import io.github.quickmsg.common.config.Configuration;
import io.github.quickmsg.common.message.MessageRegistry;
import io.github.quickmsg.common.message.EventRegistry;
import io.github.quickmsg.common.message.SmqttMessage;
import io.github.quickmsg.common.protocol.ProtocolAdaptor;
import io.github.quickmsg.common.rule.DslExecutor;
import io.github.quickmsg.common.topic.TopicRegistry;
import io.github.quickmsg.common.metric.MetricManager;
import io.netty.handler.codec.mqtt.MqttMessage;

import java.util.function.BiConsumer;

/**
 * @author luxurong
 */

public interface ReceiveContext<T extends Configuration> extends BiConsumer<MqttChannel, SmqttMessage<MqttMessage>> {


    /**
     * topic注册中心
     *
     * @return {@link TopicRegistry}
     */
    TopicRegistry getTopicRegistry();

    /**
     * channel管理中心
     *
     * @return {@link ChannelRegistry}
     */
    ChannelRegistry getChannelRegistry();


    /**
     * 协议转换器
     *
     * @return {@link ProtocolAdaptor}
     */
    ProtocolAdaptor getProtocolAdaptor();


    /**
     * 持久化消息处理
     *
     * @return {@link MessageRegistry}
     */
    MessageRegistry getMessageRegistry();


    /**
     * 集群注册器
     *
     * @return {@link ClusterRegistry}
     */
    ClusterRegistry getClusterRegistry();


    /**
     * 消息感知/设备感知
     *
     * @return {@link EventRegistry}
     */
    EventRegistry getEventRegistry();


    /**
     * 规则引擎注册器
     *
     * @return {@link DslExecutor}
     */
    DslExecutor getDslExecutor();


    /**
     * 获取配置文件
     *
     * @return {@link Configuration}
     */
    T getConfiguration();


    MetricManager getMetricManager();


    /**
     * get acl
     *
     * @return {@link  AclManager}
     */
    AclManager getAclManager();

    /**
     * 全局流控
     *
     * @return {@link TrafficHandlerLoader}
     */
    TrafficHandlerLoader getTrafficHandlerLoader();


    /**
     * 全局流控
     *
     * @return {@link TrafficHandlerLoader}
     */
    TimeAckManager getTimeAckManager();


}
