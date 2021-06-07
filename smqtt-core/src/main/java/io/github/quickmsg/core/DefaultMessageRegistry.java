package io.github.quickmsg.core;

import io.github.quickmsg.common.message.MessageRegistry;
import io.github.quickmsg.common.message.RetainMessage;
import io.github.quickmsg.common.message.SessionMessage;
import io.github.quickmsg.common.utils.TopicRegexUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author luxurong
 */
public class DefaultMessageRegistry implements MessageRegistry {


    private Map<String, RetainMessage> retainMessages = new ConcurrentHashMap<>();


    @Override
    public List<SessionMessage> getSessionMessages(String clientIdentifier) {
        return Collections.emptyList();
    }

    @Override
    public void sendSessionMessages(SessionMessage sessionMessage) {

    }

    @Override
    public void saveRetainMessage(RetainMessage retainMessage) {
        retainMessages.put(retainMessage.getTopic(), retainMessage);
    }

    @Override
    public List<RetainMessage> getRetainMessage(String topic) {
        return retainMessages.keySet()
                .stream()
                .filter(key -> key.matches(TopicRegexUtils.regexTopic(topic)))
                .map(retainMessages::get)
                .collect(Collectors.toList());
    }


//    private Map<String, RetainMessage> retainMessages = new ConcurrentHashMap<>();
//
//
//    @Override
//    public Optional<List<MqttPublishMessage>> getSessionMessages(String clientIdentifier) {
//        return Optional.empty();
//    }
//
//    @Override
//    public void sendSessionMessages(String clientIdentifier, MqttPublishMessage messages) {
//
//    }
//
//    @Override
//    public void saveRetainMessage(String topic, MqttPublishMessage messages) {
//        // clear retain message
//        if (messages.payload() == null) {
//            retainMessages.get(topic).release();
//            retainMessages.remove(topic);
//        } else {
//            retainMessages.put(topic, new RetainMessage(messages.fixedHeader().qosLevel(), messages.payload().copy()));
//        }
//    }
//
//    @Override
//    public Optional<List<MqttPublishMessage>> getRetainMessage(String topic, MqttChannel mqttChannel) {
//        String regexTopic = TopicRegexUtils.regexTopic(topic);
//        Set<String> topics = retainMessages.keySet()
//                .stream()
//                .filter(key -> key.matches(regexTopic))
//                .collect(Collectors.toSet());
//        if (topics.size() > 0) {
//            return Optional.of(topics.stream()
//                    .map(key -> {
//                        RetainMessage retainMessage = retainMessages.get(key);
//                        return MqttMessageBuilder.buildPub(false,
//                                retainMessage.getMqttQoS(),
//                                retainMessage.getMqttQoS() == MqttQoS.AT_MOST_ONCE ? 0 : mqttChannel.generateMessageId(),
//                                key,
//                                retainMessage.getByteBuf().copy());
//                    }).collect(Collectors.toList()));
//        } else {
//            return Optional.empty();
//        }
//
//
//    }
}
