package com.github.smqtt.cluster.protocol;

import com.github.smqtt.cluster.base.ClusterRegistry;
import com.github.smqtt.cluster.scalescube.ScubeClusterRegistry;
import com.github.smqtt.common.interceptor.MessageInterceptor;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttPublishMessage;

import java.util.Optional;

/**
 * @author luxurong
 * @date 2021/4/12 11:54
 * @description 集群适配器
 */
public class ClusterInterceptor implements MessageInterceptor {


    public ClusterInterceptor() {
//        this.clusterRegistry = Optional.ofNullable(ClusterRegistry.INSTANCE).orElse(new ScubeClusterRegistry());
    }


    @Override
    public Object[] doInterceptor(Object[] args) {
        MqttPublishMessage mqttMessage = (MqttPublishMessage) args[1];
        return new Object[0];
    }

    @Override
    public int order() {

        return 0;
    }

    @Override
    public MqttMessageType interceptorType() {
        return MqttMessageType.PUBLISH;
    }
}
