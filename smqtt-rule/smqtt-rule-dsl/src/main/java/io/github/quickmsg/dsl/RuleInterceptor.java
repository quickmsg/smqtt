package io.github.quickmsg.dsl;

import io.github.quickmsg.common.config.Configuration;
import io.github.quickmsg.common.context.ReceiveContext;
import io.github.quickmsg.common.interceptor.Interceptor;
import io.github.quickmsg.common.interceptor.Invocation;
import io.github.quickmsg.common.message.SmqttMessage;
import io.netty.handler.codec.mqtt.MqttMessage;

/**
 * @author luxurong
 */
public class RuleInterceptor implements Interceptor {




    @Override
    @SuppressWarnings("unchecked")
    public Object intercept(Invocation invocation) {
        SmqttMessage<MqttMessage> smqttMessage = (SmqttMessage<MqttMessage>) invocation.getArgs()[1];
        ReceiveContext<Configuration> mqttReceiveContext = (ReceiveContext<Configuration>) invocation.getArgs()[2];
        if(!smqttMessage.getIsCluster()){

        }
        return invocation.proceed();
    }

    @Override
    public int sort() {
        return 0;
    }
}
