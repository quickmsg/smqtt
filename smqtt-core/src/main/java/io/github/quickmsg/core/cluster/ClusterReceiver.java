package io.github.quickmsg.core.cluster;

import io.github.quickmsg.common.channel.MockMqttChannel;
import io.github.quickmsg.common.cluster.ClusterConfig;
import io.github.quickmsg.common.cluster.ClusterMessage;
import io.github.quickmsg.common.cluster.ClusterRegistry;
import io.github.quickmsg.common.message.MqttMessageBuilder;
import io.github.quickmsg.common.protocol.ProtocolAdaptor;
import io.github.quickmsg.core.mqtt.MqttReceiveContext;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * @author luxurong
 */
@Slf4j
public class ClusterReceiver {

    private final MqttReceiveContext mqttReceiveContext;


    public ClusterReceiver(MqttReceiveContext mqttReceiveContext) {
        this.mqttReceiveContext = mqttReceiveContext;
    }

    public void registry() {
        ClusterConfig clusterConfig = mqttReceiveContext.getConfiguration().getClusterConfig();
        ClusterRegistry clusterRegistry = mqttReceiveContext.getClusterRegistry();
        ProtocolAdaptor protocolAdaptor = mqttReceiveContext.getProtocolAdaptor();
        if (clusterConfig.getClustered()) {
            if (clusterRegistry instanceof InJvmClusterRegistry) {
                Flux.interval(Duration.ofSeconds(2))
                        .subscribe(index -> log.warn("please set  smqtt-registry dependency  "));
            } else {
                //registry cluster
                clusterRegistry.registry(clusterConfig);
                //begin listen cluster message
                clusterRegistry.handlerClusterMessage()
                        .subscribe(clusterMessage -> protocolAdaptor
                                .chooseProtocol(MockMqttChannel.
                                                DEFAULT_MOCK_CHANNEL,
                                        getMqttMessage(clusterMessage),
                                        mqttReceiveContext));
            }
        }
    }

    private MqttPublishMessage getMqttMessage(ClusterMessage clusterMessage) {
        return MqttMessageBuilder
                .buildPub(false,
                        MqttQoS.valueOf(clusterMessage.getQos()),
                        0,
                        clusterMessage.getTopic(),
                        PooledByteBufAllocator.DEFAULT.buffer().writeBytes(clusterMessage.getMessage()));
    }

}
