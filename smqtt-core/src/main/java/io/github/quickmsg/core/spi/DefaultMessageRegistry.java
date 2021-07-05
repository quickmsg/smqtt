package io.github.quickmsg.core.spi;

import io.github.quickmsg.common.message.MessageRegistry;
import io.github.quickmsg.common.message.RetainMessage;
import io.github.quickmsg.common.message.SessionMessage;
import io.github.quickmsg.common.utils.TopicRegexUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @author luxurong
 */
public class DefaultMessageRegistry implements MessageRegistry {


    private Map<String, List<SessionMessage>> sessionMessages = new ConcurrentHashMap<>();

    private Map<String, RetainMessage> retainMessages = new ConcurrentHashMap<>();


    @Override
    public List<SessionMessage> getSessionMessage(String clientIdentifier) {
        return sessionMessages.remove(clientIdentifier);
    }

    @Override
    public void saveSessionMessage(SessionMessage sessionMessage) {
        List<SessionMessage> sessionList = sessionMessages.computeIfAbsent(sessionMessage.getClientIdentifier(), key -> new CopyOnWriteArrayList<>());
        sessionList.add(sessionMessage);
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
