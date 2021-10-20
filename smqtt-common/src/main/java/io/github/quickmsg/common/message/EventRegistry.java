package io.github.quickmsg.common.message;

import io.github.quickmsg.common.channel.MqttChannel;
import io.github.quickmsg.common.context.ReceiveContext;
import io.github.quickmsg.common.enums.Event;

import java.util.Map;

/**
 * @author luxurong
 */
public interface EventRegistry {


    /**
     * message
     *
     * @param event       {@link Event}
     * @param mqttChannel {@link MqttChannel}
     * @param body        {@link Object}
     * @param receiveContext {@link ReceiveContext}
     */
    void registry(Event event, MqttChannel mqttChannel, Object body, ReceiveContext<?> receiveContext);

}
