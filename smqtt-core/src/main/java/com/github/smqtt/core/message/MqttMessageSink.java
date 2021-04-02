package com.github.smqtt.core.message;

import com.github.smqtt.common.message.ReplyMessage;
import reactor.core.Disposable;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author luxurong
 * @date 2021/4/2 18:33
 * @description
 */
public class MqttMessageSink {

    private Map<Integer, Disposable> replyMqttMessageMap = new ConcurrentHashMap<>();

    public void offerReply(ReplyMessage<?> replyMqttMessage) {
        Disposable disposable = replyMqttMessage.sendRetain()
                .delaySubscription(Duration.ofSeconds(10))
                .repeat()
                .doOnCancel(replyMqttMessage::clear)
                .subscribe();
        replyMqttMessageMap.put(replyMqttMessage.messageId(), disposable);
    }

    public void removeReply(Integer messageId) {
        Optional.ofNullable(replyMqttMessageMap.get(messageId))
                .ifPresent(Disposable::dispose);
        replyMqttMessageMap.remove(messageId);
    }

}