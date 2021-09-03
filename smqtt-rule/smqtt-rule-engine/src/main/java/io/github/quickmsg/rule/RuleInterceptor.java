package io.github.quickmsg.rule;

import io.github.quickmsg.common.config.Configuration;
import io.github.quickmsg.common.context.ReceiveContext;
import io.github.quickmsg.common.interceptor.Interceptor;
import io.github.quickmsg.common.interceptor.Invocation;
import io.github.quickmsg.common.message.SmqttMessage;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttPublishMessage;

import java.util.LinkedList;

/**
 * 规则引擎拦截器
 *
 * @author luxurong
 */

public class RuleInterceptor implements Interceptor {

    private RuleChain ruleChain = RuleChain.getSingleton();

    @Override
    @SuppressWarnings("unchecked")
    public Object intercept(Invocation invocation) {
        SmqttMessage<MqttMessage> smqttMessage = (SmqttMessage<MqttMessage>) invocation.getArgs()[1];
        ReceiveContext<Configuration> mqttReceiveContext = (ReceiveContext<Configuration>) invocation.getArgs()[2];
        MqttMessage message = smqttMessage.getMessage();

        ruleChain.getRuleNodeList().forEach(ruleNode -> {

        });

        return invocation.proceed();
    }

    @Override
    public int sort() {
        return 10000;
    }
}
