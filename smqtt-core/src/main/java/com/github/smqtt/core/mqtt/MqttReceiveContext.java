package com.github.smqtt.core.mqtt;

import com.github.smqtt.common.context.ReceiveContext;
import com.github.smqtt.common.transport.Transport;
import io.netty.handler.codec.mqtt.MqttMessage;
import lombok.Getter;
import reactor.netty.Connection;

/**
 * @author luxurong
 * @date 2021/3/30 13:25
 * @Description 服务端操作连接channel类
 */
@Getter
public class MqttReceiveContext extends ReceiveContext<MqttConfiguration> {

    private Connection connection;

    public MqttReceiveContext(MqttConfiguration configuration, Transport<MqttConfiguration> transport) {
        super(configuration, transport);
    }


    public void apply(Connection connection) {
        this.connection = connection;
        this.connection.inbound().receiveObject().cast(MqttMessage.class).subscribe();
    }


}
