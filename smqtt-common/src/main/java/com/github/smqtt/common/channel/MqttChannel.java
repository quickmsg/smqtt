package com.github.smqtt.common.channel;

import com.github.smqtt.common.enums.ChannelStatus;
import com.github.smqtt.common.message.MessageSink;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.Builder;
import lombok.Data;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.netty.Connection;

/**
 * @author luxurong
 * @date 2021/3/30 13:43
 * @description
 */
@Data
@Builder
public class MqttChannel {

    private Connection connection;

    private String clientIdentifier;

    private ChannelStatus status;

    private long activeTime;

    private long authTime;

    private boolean sessionPersistent;

    private Will will;

    private long keepalive;

    @Data
    @Builder
    public static class Will {

        private boolean isRetain;

        private String willTopic;

        private MqttQoS mqttQoS;

        private byte[] willMessage;

    }

    /**
     * 写入消息
     *
     * @param mqttMessage 消息体
     * @return boolean状态
     */
    public Mono<Void> write(MqttMessage mqttMessage) {
        return connection.outbound().sendObject(mqttMessage).then();
    }







}
