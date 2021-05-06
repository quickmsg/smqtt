package io.github.quickmsg.core.mqtt;

import io.github.quickmsg.common.channel.MqttChannel;
import io.github.quickmsg.common.cluster.ClusterMessage;
import io.github.quickmsg.common.enums.ChannelStatus;
import io.github.quickmsg.common.transport.Transport;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.netty.Connection;

import java.time.Duration;
import java.util.function.Function;

/**
 * @author luxurong
 */
@Getter
@Slf4j
public class MqttReceiveContext extends AbstractReceiveContext<MqttConfiguration> {


    private Disposable deferCloseDisposable;


    public MqttReceiveContext(MqttConfiguration configuration, Transport<MqttConfiguration> transport) {
        super(configuration, transport);

    }

    public void apply(MqttChannel mqttChannel) {
        deferCloseChannel(mqttChannel.getConnection());
        mqttChannel
                .onClose(onDisposable(mqttChannel))
                .getConnection()
                .inbound()
                .receiveObject()
                .cast(MqttMessage.class)
                .map(this.clusterTransport())
                .subscribe(mqttMessage -> this.accept(mqttChannel, mqttMessage));

    }

    private Function<MqttMessage, MqttMessage> clusterTransport() {
        return mqttMessage -> {
            if(mqttMessage instanceof MqttPublishMessage){
                this.getClusterRegistry().spreadPublishMessage((MqttPublishMessage)mqttMessage).subscribe();
                ((ByteBuf) mqttMessage.payload()).retain();
            }
            return mqttMessage;
        };
    }


    @Override
    public void accept(MqttChannel mqttChannel, MqttMessage mqttMessage) {
        log.info("accept channel] {} message {}",mqttChannel.getConnection(),mqttMessage);
        this.getProtocolAdaptor().chooseProtocol(mqttChannel, mqttMessage, this);
    }

    private MqttReceiveContext deferCloseChannel(Connection connection) {
        this.deferCloseDisposable = Mono.fromRunnable(() -> {
            if (!connection.isDisposed()) {
                connection.dispose();
            }
        }).delaySubscription(Duration.ofSeconds(10)).subscribe();
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
