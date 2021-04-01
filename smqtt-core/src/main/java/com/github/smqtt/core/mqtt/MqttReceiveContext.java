package com.github.smqtt.core.mqtt;

import com.github.smqtt.common.channel.MqttChannel;
import com.github.smqtt.common.protocol.ProtocolAdaptor;
import com.github.smqtt.common.transport.Transport;
import com.github.smqtt.core.DefaultProtocolAdaptor;
import io.netty.handler.codec.mqtt.MqttMessage;
import lombok.Getter;
import reactor.core.Disposable;

/**
 * @author luxurong
 * @date 2021/3/30 13:25
 * @Description 服务端操作连接channel类
 */
@Getter
public class MqttReceiveContext extends AbstractReceiveContext<MqttConfiguration> {


    private Disposable deferCloseDisposable;


    public MqttReceiveContext(MqttConfiguration configuration, Transport<MqttConfiguration> transport) {
        super(configuration, transport);

    }


    public void apply(MqttChannel mqttChannel, Disposable disposable) {
        this.deferCloseDisposable = disposable;
        mqttChannel
                .getConnection()
                .inbound()
                .receiveObject()
                .cast(MqttMessage.class)
                .subscribe(mqttMessage -> this.accept(mqttChannel, mqttMessage));

    }

    @Override
    public void accept(MqttChannel mqttChannel, MqttMessage mqttMessage) {
        this.getProtocolAdaptor().chooseProtocol(mqttChannel, mqttMessage, this);
    }


}
