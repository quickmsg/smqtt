package io.github.quickmsg.core.cluster;

import io.github.quickmsg.common.channel.MqttChannel;
import io.github.quickmsg.common.message.RecipientRegistry;
import io.github.quickmsg.core.mqtt.MqttReceiveContext;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import reactor.core.scheduler.Scheduler;

import java.util.function.BiFunction;

/**
 * @author luxurong
 */
public class ClusterSender implements BiFunction<MqttChannel, MqttMessage, MqttMessage> {

    private final MqttReceiveContext mqttReceiveContext;

    private final Scheduler scheduler;

    private final RecipientRegistry recipientRegistry;

    public ClusterSender(Scheduler scheduler, MqttReceiveContext mqttReceiveContext, RecipientRegistry recipientRegistry) {
        this.scheduler = scheduler;
        this.mqttReceiveContext = mqttReceiveContext;
        this.recipientRegistry = recipientRegistry;
    }

    @Override
    public MqttMessage apply(MqttChannel mqttChannel, MqttMessage message) {
        if (message instanceof MqttPublishMessage) {
            MqttPublishMessage publishMessage = (MqttPublishMessage) message;
            publishMessage.payload().resetReaderIndex();
            recipientRegistry.accept(mqttChannel, publishMessage);
            publishMessage.retain();
            if (mqttReceiveContext.getConfiguration().getClusterConfig().getClustered()) {
                mqttReceiveContext.getClusterRegistry().spreadPublishMessage(publishMessage.copy()).subscribeOn(scheduler).subscribe();
            }
        }
        return message;
    }

}
