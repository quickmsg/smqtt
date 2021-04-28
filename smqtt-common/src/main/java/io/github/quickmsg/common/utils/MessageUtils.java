package io.github.quickmsg.common.utils;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttPublishVariableHeader;
import lombok.extern.slf4j.Slf4j;

/**
 * @author luxurong
 * @description
 * @date 2021/3/29 11:21
 */
@Slf4j
public class MessageUtils {

    public static void safeRelease(MqttMessage mqttMessage) {
        if (mqttMessage.payload() instanceof ByteBuf) {
            ByteBuf byteBuf = ((ByteBuf) mqttMessage.payload());
            int count = byteBuf.refCnt();
            if (count > 0) {
                byteBuf.release(count);
                if (log.isDebugEnabled()) {
                    log.info("netty success release byteBuf {} count {} ", byteBuf, count);
                }
            }
        }
    }


    /**
     * 生成发布消息
     *
     * @param messageId 消息id
     * @param message   消息
     * @return MqttPublishMessage
     */
    public static MqttPublishMessage wrapPublishMessage(MqttPublishMessage message, int messageId) {
        MqttPublishVariableHeader mqttPublishVariableHeader = message.variableHeader();
        MqttFixedHeader mqttFixedHeader = message.fixedHeader();
        MqttFixedHeader newFixedHeader = new MqttFixedHeader(mqttFixedHeader.messageType(), false, mqttFixedHeader.qosLevel(), false, mqttFixedHeader.remainingLength());
        MqttPublishVariableHeader newHeader = new MqttPublishVariableHeader(mqttPublishVariableHeader.topicName(), messageId);
        return new MqttPublishMessage(newFixedHeader, newHeader, message.payload().copy());

    }


}
