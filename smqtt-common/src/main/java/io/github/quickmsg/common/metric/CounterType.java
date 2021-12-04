package io.github.quickmsg.common.metric;

import lombok.Getter;

/**
 * @author luxurong
 */
@Getter
public enum CounterType {

    CONNECT("smqtt.connect.count"),
    SUBSCRIBE("smqtt.subscribe.count"),

    PUBLISH_EVENT("smqtt.publish.event.count"),
    CONNECT_EVENT("smqtt.connect.event.count"),
    SUBSCRIBE_EVENT("smqtt.subscribe.event.count"),
    UN_SUBSCRIBE_EVENT("smqtt.unscribe.event.count"),
    DIS_CONNECT_EVENT("smqtt.disconnect.event.count"),
    CLOSE_EVENT("smqtt.close.event.count");

    private final String desc;

    CounterType(String desc) {
        this.desc = desc;
    }
}
