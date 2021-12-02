package io.github.quickmsg.core.http;

import io.github.quickmsg.common.channel.MockMqttChannel;
import io.github.quickmsg.common.context.ContextHolder;
import io.github.quickmsg.common.message.SmqttMessage;
import io.github.quickmsg.common.http.HttpActor;
import io.github.quickmsg.core.DefaultTransport;
import io.netty.handler.codec.mqtt.MqttPublishMessage;

/**
 * @author luxurong
 */
public abstract class AbstractHttpActor implements HttpActor {

    /**
     * 发送mqtt消息
     *
     * @param mqttPublishMessage publish消息
     */
    public void sendMqttMessage(MqttPublishMessage mqttPublishMessage) {
        ContextHolder
                .getReceiveContext()
                .getProtocolAdaptor()
                .chooseProtocol(MockMqttChannel.DEFAULT_MOCK_CHANNEL,
                        new SmqttMessage<>(mqttPublishMessage,System.currentTimeMillis(),Boolean.FALSE),
                        ContextHolder.getReceiveContext());
    }
}
