package io.github.quickmsg.core.cluster;

import io.github.quickmsg.common.channel.MqttChannel;
import io.github.quickmsg.common.cluster.ClusterMessage;
import io.github.quickmsg.common.config.Configuration;
import io.github.quickmsg.common.context.ReceiveContext;
import io.github.quickmsg.common.interceptor.Interceptor;
import io.github.quickmsg.common.interceptor.Invocation;
import io.github.quickmsg.common.message.SmqttMessage;
import io.github.quickmsg.common.utils.MessageUtils;
import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttPublishVariableHeader;
import reactor.core.scheduler.Schedulers;

/**
 * 拦截集群消息
 *
 * @author luxurong
 */
public class SpreadInterceptor implements Interceptor {

    @Override
    @SuppressWarnings("unchecked")
    public Object intercept(Invocation invocation) {
        MqttChannel mqttChannel = (MqttChannel) invocation.getArgs()[0];
        SmqttMessage<MqttMessage> smqttMessage = (SmqttMessage<MqttMessage>) invocation.getArgs()[1];
        ReceiveContext<Configuration> mqttReceiveContext = (ReceiveContext<Configuration>) invocation.getArgs()[2];
        MqttMessage message = smqttMessage.getMessage();
        if (!smqttMessage.getIsCluster() && message instanceof MqttPublishMessage) {
            MqttPublishMessage publishMessage = (MqttPublishMessage) message;
            publishMessage.retain();
            if (mqttReceiveContext.getConfiguration().getClusterConfig().getClustered()) {
                mqttReceiveContext.getClusterRegistry().spreadPublishMessage(this.clusterMessage(publishMessage, mqttChannel)).subscribeOn(Schedulers.boundedElastic()).subscribe();
            }
        }
        return invocation.proceed();
    }


    /**
     * 构建消息体
     *
     * @param message {@link MqttPublishMessage}
     * @return {@link ClusterMessage}
     */
    private ClusterMessage clusterMessage(MqttPublishMessage message, MqttChannel channel) {
        MqttPublishVariableHeader header = message.variableHeader();
        MqttFixedHeader fixedHeader = message.fixedHeader();
        return ClusterMessage.builder()
                .clientIdentifier(channel.getClientIdentifier())
                .message(MessageUtils.copyReleaseByteBuf(message.payload()))
                .topic(header.topicName())
                .retain(fixedHeader.isRetain())
                .qos(fixedHeader.qosLevel().value())
                .build();
    }

    @Override
    public int sort() {
        return 9999;
    }
}
