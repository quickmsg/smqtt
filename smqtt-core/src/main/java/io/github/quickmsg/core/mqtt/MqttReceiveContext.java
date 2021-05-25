package io.github.quickmsg.core.mqtt;

import io.github.quickmsg.common.channel.MqttChannel;
import io.github.quickmsg.common.enums.ChannelStatus;
import io.github.quickmsg.common.transport.Transport;
import io.github.quickmsg.core.cluster.ClusterReceiver;
import io.github.quickmsg.core.cluster.ClusterSender;
import io.netty.handler.codec.mqtt.MqttMessage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.netty.Connection;

import java.time.Duration;

/**
 * @author luxurong
 */
@Getter
@Slf4j
public class MqttReceiveContext extends AbstractReceiveContext<MqttConfiguration> {


    private final ClusterSender clusterSender;

    private final ClusterReceiver clusterReceiver;

    public MqttReceiveContext(MqttConfiguration configuration, Transport<MqttConfiguration> transport) {
        super(configuration, transport);
        this.clusterSender = new ClusterSender(Schedulers.newParallel("cluster-transport"), this);
        this.clusterReceiver = new ClusterReceiver(this);
        clusterReceiver.registry();
    }

    public void apply(MqttChannel mqttChannel) {
        registryCloseChannel(mqttChannel)
                .onClose(onDisposable(mqttChannel))
                .getConnection()
                .inbound()
                .receiveObject()
                .cast(MqttMessage.class)
                .map(clusterSender)
                .subscribe(mqttMessage -> this.accept(mqttChannel, mqttMessage));

    }

    @Override
    public void accept(MqttChannel mqttChannel, MqttMessage mqttMessage) {
        log.info("accept channel] {} message {}", mqttChannel.getConnection(), mqttMessage);
        this.getProtocolAdaptor().chooseProtocol(mqttChannel, mqttMessage, this);
    }

    private MqttChannel registryCloseChannel(MqttChannel mqttChannel) {
        Connection connection = mqttChannel.getConnection();
        mqttChannel.setCloseDisposable(Mono.fromRunnable(() -> {
            if (!connection.isDisposed()) {
                connection.dispose();
            }
        }).delaySubscription(Duration.ofSeconds(10)).subscribe());
        return mqttChannel;
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
