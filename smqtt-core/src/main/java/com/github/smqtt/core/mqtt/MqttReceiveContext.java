package com.github.smqtt.core.mqtt;

import com.github.smqtt.common.channel.ChannelRegistry;
import com.github.smqtt.common.channel.MqttChannel;
import com.github.smqtt.common.context.ReceiveContext;
import com.github.smqtt.common.protocol.ProtocolAdaptor;
import com.github.smqtt.common.spi.DynamicLoader;
import com.github.smqtt.common.transport.Transport;
import com.github.smqtt.core.DefaultProtocolAdaptor;
import com.github.smqtt.core.channel.DefaultChannelRegistry;
import io.netty.handler.codec.mqtt.MqttMessage;
import lombok.Getter;

import java.util.Optional;

/**
 * @author luxurong
 * @date 2021/3/30 13:25
 * @Description 服务端操作连接channel类
 */
@Getter
public class MqttReceiveContext extends ReceiveContext<MqttConfiguration> {


    public MqttReceiveContext(MqttConfiguration configuration, Transport<MqttConfiguration> transport) {
        super(configuration, transport);

    }

    @Override
    public ChannelRegistry channelRegistry(MqttConfiguration configuration) {
        return Optional.ofNullable(DynamicLoader
                .findFirst(configuration.getChannelRegistry())
                .orElse(ChannelRegistry.INSTANCE)).orElse(new DefaultChannelRegistry());
    }

    @Override
    public ProtocolAdaptor<MqttConfiguration> protocolAdaptor(MqttConfiguration configuration) {
        return new DefaultProtocolAdaptor();
    }


    public void apply(MqttChannel mqttChannel) {
        mqttChannel
                .getConnection()
                .inbound()
                .receiveObject()
                .cast(MqttMessage.class)
                .subscribe(mqttMessage -> this.accept(mqttChannel, mqttMessage));

    }

    @Override
    public void accept(MqttChannel mqttChannel, MqttMessage mqttMessage) {
        this.getProtocolAdaptor()
                .chooseProtocol(mqttChannel, mqttMessage, this);
    }
}
