package io.github.quickmsg.core.cluster;

import io.github.quickmsg.common.channel.MockMqttChannel;
import io.github.quickmsg.common.config.BootstrapConfig;
import io.github.quickmsg.common.message.HeapMqttMessage;
import io.github.quickmsg.common.cluster.ClusterRegistry;
import io.github.quickmsg.common.message.MqttMessageBuilder;
import io.github.quickmsg.common.message.SmqttMessage;
import io.github.quickmsg.common.protocol.ProtocolAdaptor;
import io.github.quickmsg.core.mqtt.MqttReceiveContext;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.handler.codec.mqtt.MqttMessage;
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
        BootstrapConfig.ClusterConfig clusterConfig = mqttReceiveContext.getConfiguration().getClusterConfig();
        ClusterRegistry clusterRegistry = mqttReceiveContext.getClusterRegistry();
        ProtocolAdaptor protocolAdaptor = mqttReceiveContext.getProtocolAdaptor();
        if (clusterConfig.isEnable()) {
            if (clusterRegistry instanceof InJvmClusterRegistry) {
                Flux.interval(Duration.ofSeconds(2))
                        .subscribe(index -> log.warn("please set  smqtt-registry dependency  "));
            } else {
                //registry cluster
                clusterRegistry.registry(clusterConfig);
                //begin listen cluster message
                clusterRegistry.handlerClusterMessage()
                        .subscribe(clusterMessage -> protocolAdaptor
                                .chooseProtocol(MockMqttChannel.wrapClientIdentifier(clusterMessage.getClientIdentifier()),
                                        getMqttMessage(clusterMessage),
                                        mqttReceiveContext));
            }
        }
    }

    private SmqttMessage<MqttMessage> getMqttMessage(HeapMqttMessage heapMqttMessage) {
        return new SmqttMessage<>(MqttMessageBuilder
                .buildPub(false,
                        MqttQoS.valueOf(heapMqttMessage.getQos()),
                        0,
                        heapMqttMessage.getTopic(),
                        PooledByteBufAllocator.DEFAULT.buffer().writeBytes(heapMqttMessage.getMessage()),
                        heapMqttMessage.getProperties()), System.currentTimeMillis(), Boolean.TRUE);
    }

}
