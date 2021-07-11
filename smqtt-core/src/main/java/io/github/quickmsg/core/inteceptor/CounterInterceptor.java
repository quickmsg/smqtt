package io.github.quickmsg.core.inteceptor;

import io.github.quickmsg.common.interceptor.Interceptor;
import io.github.quickmsg.common.interceptor.Invocation;
import io.github.quickmsg.core.metric.MetricManager;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * @author luxurong
 */
@Slf4j
public class CounterInterceptor implements Interceptor {


    @Override
    public Object intercept(Invocation invocation) {
        Object[] args = invocation.getArgs();
        MqttMessage mqttMessage = (MqttMessage) args[1];
        if (mqttMessage instanceof MqttPublishMessage) {
            MetricManager.registryMetric((MqttPublishMessage) mqttMessage);
        }
        return invocation.proceed();
    }

    @Override
    public int sort() {
        return 999;
    }
}
