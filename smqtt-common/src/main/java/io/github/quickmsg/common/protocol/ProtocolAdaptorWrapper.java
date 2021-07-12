package io.github.quickmsg.common.protocol;

import io.github.quickmsg.common.channel.MqttChannel;
import io.github.quickmsg.common.config.Configuration;
import io.github.quickmsg.common.context.ReceiveContext;
import io.github.quickmsg.common.protocol.ProtocolAdaptor;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import reactor.core.scheduler.Schedulers;

/**
 * @author luxurong
 */
public class ProtocolAdaptorWrapper implements ProtocolAdaptor {

    private final ProtocolAdaptor protocolAdaptor;

    public ProtocolAdaptorWrapper(ProtocolAdaptor protocolAdaptor) {
        this.protocolAdaptor = protocolAdaptor;
    }


    @Override
    public <C extends Configuration> void chooseProtocol(MqttChannel mqttChannel, MqttMessage mqttMessage, ReceiveContext<C> receiveContext) {
        if (receiveContext.getConfiguration().getClusterConfig().getClustered() && mqttMessage instanceof MqttPublishMessage) {
            receiveContext.getClusterRegistry().spreadPublishMessage(((MqttPublishMessage) mqttMessage).copy()).subscribeOn(Schedulers.single()).subscribe();
        }
        protocolAdaptor.chooseProtocol(mqttChannel, mqttMessage, receiveContext);
    }

}
