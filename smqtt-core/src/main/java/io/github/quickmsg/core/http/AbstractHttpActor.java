package io.github.quickmsg.core.http;

import io.github.quickmsg.common.channel.MockMqttChannel;
import io.github.quickmsg.common.message.SmqttMessage;
import io.github.quickmsg.common.protocol.ProtocolAdaptorWrapper;
import io.github.quickmsg.common.http.HttpActor;
import io.github.quickmsg.common.protocol.ProtocolAdaptor;
import io.github.quickmsg.core.DefaultTransport;
import io.netty.handler.codec.mqtt.MqttPublishMessage;

/**
 * @author luxurong
 */
public abstract class AbstractHttpActor implements HttpActor {

    private final ProtocolAdaptor protocolAdaptor = new ProtocolAdaptorWrapper(DefaultTransport.receiveContext.getProtocolAdaptor());

    /**
     * 发送mqtt消息
     *
     * @param mqttPublishMessage publish消息
     */
    public void sendMqttMessage(MqttPublishMessage mqttPublishMessage) {
        getProtocolAdaptor().chooseProtocol(MockMqttChannel.DEFAULT_MOCK_CHANNEL, new SmqttMessage<>(mqttPublishMessage,true), DefaultTransport.receiveContext);
    }

    public ProtocolAdaptor getProtocolAdaptor() {
        return this.protocolAdaptor;
    }
}
