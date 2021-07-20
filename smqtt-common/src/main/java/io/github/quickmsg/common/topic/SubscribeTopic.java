package io.github.quickmsg.common.topic;

import io.github.quickmsg.common.channel.MqttChannel;
import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

/**
 * @author luxurong
 */

@Getter
@Setter
@ToString
public class SubscribeTopic {

    private final String topicFilter;

    private final MqttQoS qoS;

    private final MqttChannel mqttChannel;

    public SubscribeTopic(String topicFilter, MqttQoS qoS, MqttChannel mqttChannel) {
        this.topicFilter = topicFilter;
        this.qoS = qoS;
        this.mqttChannel = mqttChannel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubscribeTopic that = (SubscribeTopic) o;
        return Objects.equals(topicFilter, that.topicFilter) &&
                Objects.equals(mqttChannel, that.mqttChannel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topicFilter, mqttChannel);
    }

    public SubscribeTopic compareQos(MqttQoS mqttQoS) {
        MqttQoS minQos = MqttQoS.valueOf(Math.min(mqttQoS.value(), qoS.value()));
        return new SubscribeTopic(topicFilter, minQos, mqttChannel);
    }

    public void linkSubscribe() {
        mqttChannel.getTopics().add(this);
    }

    public void unLinkSubscribe() {
        mqttChannel.getTopics().remove(this);
    }
}
