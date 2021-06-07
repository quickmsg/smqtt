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

}
