package io.github.quickmsg.common.interceptor;

import io.github.quickmsg.common.channel.MqttChannel;
import io.github.quickmsg.common.config.Configuration;
import io.github.quickmsg.common.context.ReceiveContext;
import io.github.quickmsg.common.message.HeapMqttMessage;
import io.github.quickmsg.common.message.SmqttMessage;
import io.github.quickmsg.common.protocol.ProtocolAdaptor;
import io.github.quickmsg.common.rule.DslExecutor;
import io.github.quickmsg.common.spi.DynamicLoader;
import io.github.quickmsg.common.utils.MessageUtils;
import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttPublishVariableHeader;
import reactor.core.scheduler.Schedulers;
import reactor.netty.ReactorNetty;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author luxurong
 */
public class MessageProxy {

    private List<Interceptor> interceptors = DynamicLoader.findAll(Interceptor.class)
            .sorted(Comparator.comparing(Interceptor::sort))
            .collect(Collectors.toList());

    public ProtocolAdaptor proxy(ProtocolAdaptor protocolAdaptor) {
        protocolAdaptor = new TailIntercept().proxyProtocol(protocolAdaptor);
        for (Interceptor interceptor : interceptors) {
            protocolAdaptor = interceptor.proxyProtocol(protocolAdaptor);
        }
        return new HeadIntercept().proxyProtocol(protocolAdaptor);
    }

    static class TailIntercept implements Interceptor {

        @Override
        @SuppressWarnings("unchecked")
        public Object intercept(Invocation invocation) {
            MqttChannel mqttChannel = (MqttChannel) invocation.getArgs()[0];
            SmqttMessage<MqttMessage> smqttMessage = (SmqttMessage<MqttMessage>) invocation.getArgs()[1];
            ReceiveContext<Configuration> mqttReceiveContext = (ReceiveContext<Configuration>) invocation.getArgs()[2];
            DslExecutor dslExecutor = mqttReceiveContext.getDslExecutor();
            MqttMessage message = smqttMessage.getMessage();
            if (!smqttMessage.getIsCluster() && message instanceof MqttPublishMessage) {
                MqttPublishMessage publishMessage = (MqttPublishMessage) message;
                HeapMqttMessage heapMqttMessage = this.clusterMessage(publishMessage, mqttChannel, smqttMessage.getTimestamp());
                if (mqttReceiveContext.getConfiguration().getClusterConfig().isEnable()) {
                    mqttReceiveContext.getClusterRegistry().spreadPublishMessage(heapMqttMessage).subscribeOn(Schedulers.boundedElastic()).subscribe();
                }
                if (dslExecutor.isExecute()) {
                    dslExecutor.executeRule(mqttChannel, heapMqttMessage, mqttReceiveContext);
                }
            }
            return invocation.proceed();
        }


        /**
         * 构建消息体
         *
         * @param message   {@link MqttPublishMessage}
         * @param timestamp
         * @return {@link HeapMqttMessage}
         */
        private HeapMqttMessage clusterMessage(MqttPublishMessage message, MqttChannel channel, long timestamp) {
            MqttPublishVariableHeader header = message.variableHeader();
            MqttFixedHeader fixedHeader = message.fixedHeader();
            return HeapMqttMessage.builder()
                    .timestamp(timestamp)
                    .clientIdentifier(channel.getClientIdentifier())
                    .message(MessageUtils.copyReleaseByteBuf(message.payload()))
                    .topic(header.topicName())
                    .retain(fixedHeader.isRetain())
                    .qos(fixedHeader.qosLevel().value())
                    .properties(header.properties())
                    .build();
        }

        @Override
        public int sort() {
            return 0;
        }
    }

    static class HeadIntercept implements Interceptor {

        @Override
        @SuppressWarnings("unchecked")
        public Object intercept(Invocation invocation) {
            SmqttMessage<MqttMessage> smqttMessage = (SmqttMessage<MqttMessage>) invocation.getArgs()[1];
            MqttMessage message = smqttMessage.getMessage();
            try {
                if (message instanceof MqttPublishMessage) {
                    MqttPublishMessage publishMessage = (MqttPublishMessage) message;
                    publishMessage.retain();
                }
                return invocation.proceed();
            } finally {
                if (smqttMessage.getIsCluster()) {
                    ReactorNetty.safeRelease(message.payload());
                }
            }

        }

        @Override
        public int sort() {
            return 0;
        }
    }

}
