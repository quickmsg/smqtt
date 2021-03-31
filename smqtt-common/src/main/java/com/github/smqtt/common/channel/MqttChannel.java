package com.github.smqtt.common.channel;

import com.github.smqtt.common.enums.ChannelStatus;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.Builder;
import lombok.Data;
import reactor.core.publisher.Mono;
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

    private String deviceId;

    private ChannelStatus status;

    private long activeTime;

    private boolean sessionPersistent;

    private Will will;

    private long keepalive;

    @Data
    public static class Will {

        private boolean isRetain;

        private String willTopic;

        private MqttQoS mqttQoS;

        private String willMessage;

    }

    public void write(ByteBuf byteBuf) {
        connection.outbound().send(Mono.just(byteBuf)).then().subscribe();
    }


}
