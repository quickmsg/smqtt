package io.github.quickmsg.common.enums;

import io.github.quickmsg.common.channel.MqttChannel;
import io.github.quickmsg.common.context.ReceiveContext;
import io.github.quickmsg.common.message.MqttMessageBuilder;
import io.github.quickmsg.common.message.SmqttMessage;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttQoS;

/**
 * @author luxurong
 * @date 2021/10/19 21:39
 */
public enum Event {


    /**
     * 连接事件
     */
    CONNECT {
        private static final String CONNECT_TOPIC = "$event/connect";

        @Override
        public void sender(MqttChannel mqttChannel, Object body, ReceiveContext<?> receiveContext) {
            receiveContext.getProtocolAdaptor()
                    .chooseProtocol(mqttChannel, new SmqttMessage<>(
                                    MqttMessageBuilder.buildPub(
                                            false,
                                            MqttQoS.AT_MOST_ONCE,
                                            0,
                                            CONNECT_TOPIC,
                                            null)
                                    , System.currentTimeMillis(), Boolean.FALSE),
                            receiveContext);
        }
    },
    /**
     * 关闭事件
     */
    CLOSE {
        private static final String CONNECT_TOPIC = "$event/close";

        @Override
        public void sender(MqttChannel mqttChannel, Object body, ReceiveContext<?> receiveContext) {
            MqttPublishMessage mqttPublishMessage =
                    MqttMessageBuilder.buildPub(false, MqttQoS.AT_MOST_ONCE, 0, CONNECT_TOPIC, null);
            write(receiveContext, mqttChannel, mqttPublishMessage);
        }
    };

    /**
     * 处理事件消息发送
     *
     * @param mqttChannel    {@link MqttChannel }
     * @param body           {@link Object }
     * @param receiveContext {@link ReceiveContext }
     */
    public abstract void sender(MqttChannel mqttChannel, Object body, ReceiveContext<?> receiveContext);


    public void write(ReceiveContext<?> receiveContext, MqttChannel mqttChannel, MqttMessage message) {
        receiveContext.getProtocolAdaptor()
                .chooseProtocol(mqttChannel, new SmqttMessage<>(
                                message
                                , System.currentTimeMillis(), Boolean.FALSE),
                        receiveContext);
    }

}
