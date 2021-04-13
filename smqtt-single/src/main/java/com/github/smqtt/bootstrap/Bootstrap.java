package com.github.smqtt.bootstrap;

import com.github.smqtt.common.auth.PasswordAuthentication;
import com.github.smqtt.common.channel.ChannelRegistry;
import com.github.smqtt.common.message.MessageRegistry;
import com.github.smqtt.common.protocol.ProtocolAdaptor;
import com.github.smqtt.common.topic.TopicRegistry;
import com.github.smqtt.common.transport.Transport;
import com.github.smqtt.core.mqtt.MqttConfiguration;
import com.github.smqtt.core.mqtt.MqttTransportFactory;
import com.github.smqtt.core.websocket.WebSocketMqttTransportFactory;
import io.netty.channel.ChannelOption;
import io.netty.channel.WriteBufferWaterMark;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author luxurong
 * @date 2021/4/13 11:46
 * @description
 */
@Builder
@Getter
@ToString
@Slf4j
public class Bootstrap {

    private static final Sinks.One<Void> START_ONLY_MQTT = Sinks.one();

    private static final Integer DEFAULT_WEBSOCKET_MQTT_PORT = 9997;


    private Integer port;

    private Integer lowWaterMark;

    private Integer highWaterMark;

    private Boolean ssl;

    private Boolean wiretap;

    private Integer bossThreadSize;

    private Integer workThreadSize;

    @Builder.Default
    private Boolean isWebsocket = false;

    @Builder.Default
    private Integer websocketPort = 0;

    private PasswordAuthentication reactivePasswordAuth;

    private Class<? extends ChannelRegistry> channelRegistry;

    private Class<? extends TopicRegistry> topicRegistry;

    private Class<? extends ProtocolAdaptor> protocolAdaptor;

    private Class<? extends MessageRegistry> messageRegistry;


    private Consumer<Map<ChannelOption<?>, ?>> options;

    private Consumer<Map<ChannelOption<?>, ?>> childOptions;


    private MqttConfiguration initMqttConfiguration() {
        MqttConfiguration mqttConfiguration = defaultConfiguration();
        Optional.ofNullable(options).ifPresent(mqttConfiguration::setOptions);
        Optional.ofNullable(childOptions).ifPresent(mqttConfiguration::setChildOptions);
        Optional.ofNullable(reactivePasswordAuth).ifPresent(mqttConfiguration::setReactivePasswordAuth);
        Optional.ofNullable(channelRegistry).ifPresent(mqttConfiguration::setChannelRegistry);
        Optional.ofNullable(topicRegistry).ifPresent(mqttConfiguration::setTopicRegistry);
        Optional.ofNullable(protocolAdaptor).ifPresent(mqttConfiguration::setProtocolAdaptor);
        Optional.ofNullable(port).ifPresent(mqttConfiguration::setPort);
        Optional.ofNullable(lowWaterMark).ifPresent(mqttConfiguration::setLowWaterMark);
        Optional.ofNullable(highWaterMark).ifPresent(mqttConfiguration::setHighWaterMark);
        Optional.ofNullable(wiretap).ifPresent(mqttConfiguration::setWiretap);
        Optional.ofNullable(bossThreadSize).ifPresent(mqttConfiguration::setBossThreadSize);
        Optional.ofNullable(workThreadSize).ifPresent(mqttConfiguration::setWorkThreadSize);
        Optional.ofNullable(messageRegistry).ifPresent(mqttConfiguration::setMessageRegistry);
        if (isWebsocket) {
            mqttConfiguration.setWebSocketPort(websocketPort);
        }
        return mqttConfiguration;
    }


    private MqttConfiguration defaultConfiguration() {
        MqttConfiguration mqttConfiguration = new MqttConfiguration();
        mqttConfiguration.setLowWaterMark(WriteBufferWaterMark.DEFAULT.low());
        mqttConfiguration.setHighWaterMark(WriteBufferWaterMark.DEFAULT.high());
        mqttConfiguration.setPort(0);
        mqttConfiguration.setWiretap(false);
        return mqttConfiguration;
    }


    /**
     * 阻塞启动 生产环境慎用
     *
     * @return void
     */
    public void startAwait() {
        this.start()
                .doOnError(err -> {
                    log.info("bootstrap server start error", err);
                    START_ONLY_MQTT.tryEmitEmpty();
                })
                .subscribe();
        START_ONLY_MQTT.asMono().block();
    }


    /**
     * 启动服务
     *
     * @return Mono
     */
    public Mono<Transport> start() {
        MqttConfiguration mqttConfiguration = initMqttConfiguration();
        if (isWebsocket) {
            WebSocketMqttTransportFactory webSocketMqttTransportFactory = new WebSocketMqttTransportFactory();
            webSocketMqttTransportFactory.createTransport(mqttConfiguration)
                    .start()
                    .doOnError(Throwable::printStackTrace)
                    .block();
        }
        MqttTransportFactory mqttTransportFactory = new MqttTransportFactory();
        return mqttTransportFactory.createTransport(mqttConfiguration)
                .start()
                .doOnError(Throwable::printStackTrace);
    }


}
