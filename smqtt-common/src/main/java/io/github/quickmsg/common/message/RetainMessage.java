package io.github.quickmsg.common.message;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.AllArgsConstructor;
import lombok.Getter;
import reactor.netty.ReactorNetty;

/**
 * @author luxurong
 * @date 2021/4/7 16:36
 * @description
 */
@Getter
@AllArgsConstructor
public class RetainMessage {

    private MqttQoS mqttQoS;

    private ByteBuf byteBuf;

    public void release() {
        ReactorNetty.safeRelease(byteBuf);
    }


}
