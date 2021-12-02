package io.github.quickmsg.common.metric;

import lombok.Getter;

/**
 * @author luxurong
 */
@Getter
public enum CounterType {

    PUBLISH("smqtt.publish.count"),
    CONNECT("smqtt.connect.count"),
    SUBSCRIBE("smqtt.subscribe.count"),
    UN_SUBSCRIBE("smqtt.unscribe.count"),
    DIS_CONNECT("smqtt.disconnect.count"),
    READ("read"),
    WRITE("write");
    private final String desc;

    CounterType(String desc) {
        this.desc = desc;
    }
}
