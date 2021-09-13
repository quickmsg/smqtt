package io.github.quickmsg.dsl;

import io.github.quickmsg.common.config.Configuration;
import io.github.quickmsg.common.context.ReceiveContext;
import io.github.quickmsg.common.interceptor.Interceptor;
import io.github.quickmsg.common.interceptor.Invocation;
import io.github.quickmsg.common.message.SmqttMessage;
import io.github.quickmsg.common.rule.DslExecutor;
import io.github.quickmsg.common.rule.RuleData;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttPublishMessage;

/**
 * @author luxurong
 */
public class RuleInterceptor implements Interceptor {


    @Override
    @SuppressWarnings("unchecked")
    public Object intercept(Invocation invocation) {
        SmqttMessage<MqttMessage> smqttMessage = (SmqttMessage<MqttMessage>) invocation.getArgs()[1];
        ReceiveContext<Configuration> mqttReceiveContext = (ReceiveContext<Configuration>) invocation.getArgs()[2];
        DslExecutor dslExecutor = mqttReceiveContext.getDslExecutor();
        if (dslExecutor.isExecute() && !smqttMessage.getIsCluster() && smqttMessage.getMessage() instanceof MqttPublishMessage) {
            MqttPublishMessage publishMessage = (MqttPublishMessage) smqttMessage.getMessage();
            dslExecutor.executeRule(mqttReceiveContext, new RuleData());
        }
        return invocation.proceed();
    }

    @Override
    public int sort() {
        return 0;
    }
}
