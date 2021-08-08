package io.github.quickmsg.common.interceptor;

import io.github.quickmsg.common.config.Configuration;
import io.github.quickmsg.common.context.ReceiveContext;
import io.github.quickmsg.common.message.SmqttMessage;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
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
        SmqttMessage<MqttMessage> smqttMessage = (SmqttMessage<MqttMessage>) invocation.getArgs()[1];
        ReceiveContext<Configuration> mqttReceiveContext = (ReceiveContext<Configuration>) invocation.getArgs()[2];
        MqttMessage message = smqttMessage.getMessage();
        if (!smqttMessage.getIsCluster() && message instanceof MqttPublishMessage) {
            MqttPublishMessage publishMessage = (MqttPublishMessage) message;
            publishMessage.retain();
            if (mqttReceiveContext.getConfiguration().getClusterConfig().getClustered()) {
                mqttReceiveContext.getClusterRegistry().spreadPublishMessage(publishMessage.copy()).subscribeOn(Schedulers.boundedElastic()).subscribe();
            }
        }
        return invocation.proceed();
    }

    @Override
    public int sort() {
        return 0;
    }
}
