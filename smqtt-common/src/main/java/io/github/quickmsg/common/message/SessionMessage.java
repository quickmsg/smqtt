package io.github.quickmsg.common.message;

import java.util.HashMap;
import java.util.Optional;

import io.github.quickmsg.common.channel.MqttChannel;
import io.github.quickmsg.common.utils.JacksonUtil;
import io.github.quickmsg.common.utils.MessageUtils;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.handler.codec.mqtt.MqttProperties;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttPublishVariableHeader;
import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.Builder;
import lombok.Data;

/**
 * @author luxurong
 */

@Data
@Builder
public class SessionMessage {

    private int qos;

    private String topic;

    private byte[] body;

    private String clientIdentifier;

    private boolean retain;

    private String userProperties;

    public static SessionMessage of(String clientIdentifier, MqttPublishMessage mqttPublishMessage) {
        MqttPublishVariableHeader publishVariableHeader = mqttPublishMessage.variableHeader();
        return SessionMessage.builder()
                .clientIdentifier(clientIdentifier)
                .topic(publishVariableHeader.topicName())
                .qos(mqttPublishMessage.fixedHeader().qosLevel().value())
                .retain(mqttPublishMessage.fixedHeader().isRetain())
                .body(MessageUtils.copyByteBuf(mqttPublishMessage.payload()))
                .userProperties(JacksonUtil.map2Json(Optional.ofNullable(publishVariableHeader
                        .properties()
                        .getProperties(MqttProperties.MqttPropertyType.USER_PROPERTY.value()))
                        .map(list -> {
                            HashMap<String, String> propertiesMap = new HashMap<>(list.size());
                            list.forEach(property -> {
                                MqttProperties.StringPair pair = (MqttProperties.StringPair) property.value();
                                propertiesMap.put(pair.key, pair.value);
                            });
                            return propertiesMap;
                        }).orElseGet(HashMap::new)))
                .build();
    }

    public MqttPublishMessage toPublishMessage(MqttChannel mqttChannel) {
        return MqttMessageBuilder.buildPub(
                false,
                MqttQoS.valueOf(this.qos),
                qos > 0 ? mqttChannel.generateMessageId() : 0,
                topic,
                PooledByteBufAllocator.DEFAULT.directBuffer().writeBytes(body),
                JacksonUtil.json2Map(userProperties, String.class, String.class));
    }

}
