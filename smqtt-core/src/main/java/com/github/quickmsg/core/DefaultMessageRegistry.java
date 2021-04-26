package com.github.quickmsg.core;

import com.github.quickmsg.common.channel.MqttChannel;
import com.github.quickmsg.common.message.MessageRegistry;
import com.github.quickmsg.common.message.MqttMessageBuilder;
import com.github.quickmsg.common.message.RetainMessage;
import com.github.quickmsg.common.utils.TopicRegexUtils;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttQoS;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author luxurong
 * @date 2021/4/4 12:53
 * @description
 */
public class DefaultMessageRegistry implements MessageRegistry {

    private Map<String, RetainMessage> retainMessages = new ConcurrentHashMap<>();


    @Override
    public Optional<List<MqttPublishMessage>> getSessionMessages(String clientIdentifier) {
        return Optional.empty();
    }

    @Override
    public void sendSessionMessages(String clientIdentifier, MqttPublishMessage messages) {

    }

    @Override
    public void saveRetainMessage(String topic, MqttPublishMessage messages) {
        // clear retain message
        if (messages.payload() == null) {
            retainMessages.get(topic).release();
            retainMessages.remove(topic);
        } else {
            retainMessages.put(topic, new RetainMessage(messages.fixedHeader().qosLevel(), messages.payload().copy()));
        }
    }

    @Override
    public Optional<List<MqttPublishMessage>> getRetainMessage(String topic, MqttChannel mqttChannel) {
        String regexTopic = TopicRegexUtils.regexTopic(topic);
        Set<String> topics = retainMessages.keySet()
                .stream()
                .filter(key -> key.matches(regexTopic))
                .collect(Collectors.toSet());
        if (topics.size() > 0) {
            return Optional.of(topics.stream()
                    .map(key -> {
                        RetainMessage retainMessage = retainMessages.get(key);
                        return MqttMessageBuilder.buildPub(false,
                                retainMessage.getMqttQoS(),
                                retainMessage.getMqttQoS() == MqttQoS.AT_MOST_ONCE ? 0 : mqttChannel.generateMessageId(),
                                key,
                                retainMessage.getByteBuf().copy());
                    }).collect(Collectors.toList()));
        } else {
            return Optional.empty();
        }


    }


}
