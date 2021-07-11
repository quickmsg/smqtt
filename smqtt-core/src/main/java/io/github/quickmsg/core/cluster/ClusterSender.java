package io.github.quickmsg.core.cluster;

import io.github.quickmsg.core.mqtt.MqttReceiveContext;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import reactor.core.scheduler.Scheduler;

import java.util.function.Function;

/**
 * @author luxurong
 */
public class ClusterSender implements Function<MqttMessage, MqttMessage> {

    private final MqttReceiveContext mqttReceiveContext;

    private final Scheduler scheduler;

    public ClusterSender(Scheduler scheduler, MqttReceiveContext mqttReceiveContext) {
        this.scheduler = scheduler;
        this.mqttReceiveContext = mqttReceiveContext;
    }

    @Override
    public MqttMessage apply(MqttMessage mqttMessage) {
        if (mqttMessage instanceof MqttPublishMessage) {
            ((MqttPublishMessage) mqttMessage).retain();
            if (mqttReceiveContext.getConfiguration().getClusterConfig().getClustered()) {
                mqttReceiveContext.getClusterRegistry().spreadPublishMessage(((MqttPublishMessage) mqttMessage).copy()).subscribeOn(scheduler).subscribe();
            }
        }
        return mqttMessage;
    }

}
