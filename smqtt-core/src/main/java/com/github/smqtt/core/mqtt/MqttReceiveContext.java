package com.github.smqtt.core.mqtt;

import com.github.smqtt.common.channel.MqttChannel;
import com.github.smqtt.common.enums.ChannelStatus;
import com.github.smqtt.common.transport.Transport;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.mqtt.MqttMessage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.netty.Connection;

import java.time.Duration;
import java.util.function.Function;

/**
 * @author luxurong
 * @date 2021/3/30 13:25
 * @Description 服务端操作连接channel类
 */
@Getter
@Slf4j
public class MqttReceiveContext extends AbstractReceiveContext<MqttConfiguration> {


    private Disposable deferCloseDisposable;


    public MqttReceiveContext(MqttConfiguration configuration, Transport<MqttConfiguration> transport) {
        super(configuration, transport);

    }

    // todo 消息持久化之后处理
    public void apply(MqttChannel mqttChannel) {
        deferCloseChannel(mqttChannel.getConnection());
        mqttChannel
                .onClose(onDisposable(mqttChannel))
                .getConnection()
                .inbound()
                .receiveObject()
                .cast(MqttMessage.class)
                .map(this.retainMessage())
                .subscribe(mqttMessage -> this.accept(mqttChannel, mqttMessage));

    }

    private Function<MqttMessage, MqttMessage> retainMessage() {
        return mqttMessage -> {
            if (mqttMessage.payload() instanceof ByteBuf) {
                ((ByteBuf) mqttMessage.payload()).retain();
            }
            return mqttMessage;
        };
    }


    @Override
    public void accept(MqttChannel mqttChannel, MqttMessage mqttMessage) {
        this.getProtocolAdaptor().chooseProtocol(mqttChannel, mqttMessage, this);
    }

    private MqttReceiveContext deferCloseChannel(Connection connection) {
        this.deferCloseDisposable = Mono.fromRunnable(() -> {
            if (!connection.isDisposed()) {
                connection.dispose();
            }
        }).delaySubscription(Duration.ofSeconds(2)).subscribe();
        return this;
    }

    private Disposable onDisposable(MqttChannel mqttChannel) {
        return () -> {
            if (mqttChannel.isSessionPersistent()) {
                mqttChannel.setStatus(ChannelStatus.OFFLINE);
                mqttChannel.close().subscribe();
            } else {
                getTopicRegistry().clear(mqttChannel);
                getChannelRegistry().close(mqttChannel);
            }
        };

    }


}
