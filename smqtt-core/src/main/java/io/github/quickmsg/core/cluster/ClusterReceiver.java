package io.github.quickmsg.core.cluster;

import io.github.quickmsg.common.channel.MockMqttChannel;
import io.github.quickmsg.common.channel.MqttChannel;
import io.github.quickmsg.common.config.BootstrapConfig;
import io.github.quickmsg.common.message.*;
import io.github.quickmsg.common.cluster.ClusterRegistry;
import io.github.quickmsg.common.protocol.ProtocolAdaptor;
import io.github.quickmsg.common.utils.JacksonUtil;
import io.github.quickmsg.core.mqtt.MqttReceiveContext;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Optional;

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
                        .doOnError(throwable -> log.error("cluster accept",throwable))
                        .onErrorResume(e-> Mono.empty())
                        .subscribe(clusterMessage -> {
                            if(clusterMessage.getClusterEvent() == ClusterMessage.ClusterEvent.PUBLISH){
                                HeapMqttMessage heapMqttMessage = (HeapMqttMessage)clusterMessage.getMessage();
                                protocolAdaptor
                                        .chooseProtocol(MockMqttChannel.wrapClientIdentifier(heapMqttMessage.getClientIdentifier()),
                                                getMqttMessage(heapMqttMessage),
                                                mqttReceiveContext);
                            }
                            else {
                                CloseMqttMessage closeMqttMessage =(CloseMqttMessage) clusterMessage.getMessage();
                                Optional.ofNullable(mqttReceiveContext.getChannelRegistry().get(closeMqttMessage.getClientIdentifier()))
                                        .ifPresent(mqttChannel -> mqttChannel.close().subscribe());

                            }

                        });
            }
        }
    }

    private SmqttMessage<MqttMessage> getMqttMessage(HeapMqttMessage heapMqttMessage) {
        return new SmqttMessage<>(MqttMessageBuilder
                .buildPub(false,
                        MqttQoS.valueOf(heapMqttMessage.getQos()),
                        0,
                        heapMqttMessage.getTopic(),
                        PooledByteBufAllocator.DEFAULT.buffer().writeBytes(JacksonUtil.dynamicJson(heapMqttMessage.getMessage()).getBytes(StandardCharsets.UTF_8)),
                        heapMqttMessage.getProperties()), System.currentTimeMillis(), Boolean.TRUE);
    }

}
