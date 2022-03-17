package io.github.quickmsg.common.enums;

import io.github.quickmsg.common.channel.MockMqttChannel;
import io.github.quickmsg.common.channel.MqttChannel;
import io.github.quickmsg.common.context.ReceiveContext;
import io.github.quickmsg.common.message.MqttMessageBuilder;
import io.github.quickmsg.common.message.SmqttMessage;
import io.github.quickmsg.common.message.system.ChannelStatusMessage;
import io.github.quickmsg.common.utils.JacksonUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttQoS;

/**
 * @author luxurong
 */
public enum Event {


    /**
     * 连接事件
     */
    CONNECT {
        private static final String CONNECT_TOPIC = "$event/connect";

        @Override
        public void sender(MqttChannel mqttChannel, Object body, ReceiveContext<?> receiveContext) {
            MqttPublishMessage mqttPublishMessage =
                    MqttMessageBuilder.buildPub(false, MqttQoS.AT_MOST_ONCE, 0, CONNECT_TOPIC, writeBody(mqttChannel, body));
            write(receiveContext, mqttChannel, mqttPublishMessage);
        }

        @Override
        public ByteBuf writeBody(MqttChannel mqttChannel, Object body) {
            return PooledByteBufAllocator.DEFAULT
                    .directBuffer().writeBytes(JacksonUtil.bean2Json(new ChannelStatusMessage(
                            mqttChannel.getClientIdentifier(),
                            System.currentTimeMillis(),
                            mqttChannel.getUsername(),
                            ChannelStatus.ONLINE)).getBytes());
        }

    },
    /**
     * 关闭事件
     */
    CLOSE {
        private static final String CLOSE_TOPIC = "$event/close";

        @Override
        public void sender(MqttChannel mqttChannel, Object body, ReceiveContext<?> receiveContext) {
            MqttPublishMessage mqttPublishMessage =
                    MqttMessageBuilder.buildPub(false, MqttQoS.AT_MOST_ONCE, 0, CLOSE_TOPIC, writeBody(mqttChannel, body));
            write(receiveContext, mqttChannel, mqttPublishMessage);
        }

        @Override
        public ByteBuf writeBody(MqttChannel mqttChannel, Object body) {
            return PooledByteBufAllocator.DEFAULT
                    .directBuffer().writeBytes(JacksonUtil.bean2Json(new ChannelStatusMessage(
                            mqttChannel.getClientIdentifier(),
                            System.currentTimeMillis(),
                            mqttChannel.getUsername(),
                            ChannelStatus.OFFLINE)).getBytes());
        }
    };

    /**
     * write event
     *
     * @param mqttChannel    {@link MqttChannel }
     * @param body           {@link Object }
     * @param receiveContext {@link ReceiveContext }
     */
    public abstract void sender(MqttChannel mqttChannel, Object body, ReceiveContext<?> receiveContext);


    /**
     * body
     *
     * @param mqttChannel {@link MqttChannel }
     * @param body        {@link Object }
     * @return ByteBuf
     */
    public abstract ByteBuf writeBody(MqttChannel mqttChannel, Object body);


    public void write(ReceiveContext<?> receiveContext, MqttChannel mqttChannel, MqttMessage message) {
        receiveContext.getProtocolAdaptor()
                .chooseProtocol(MockMqttChannel.wrapClientIdentifier(mqttChannel.getClientIdentifier()), new SmqttMessage<>(
                                message
                                , System.currentTimeMillis(), Boolean.FALSE),
                        receiveContext);
        if (message instanceof MqttPublishMessage) {
            ((MqttPublishMessage) message).release();
        }
    }

}
