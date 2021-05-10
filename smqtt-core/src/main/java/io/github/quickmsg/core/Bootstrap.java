package io.github.quickmsg.core;

import io.github.quickmsg.common.auth.PasswordAuthentication;
import io.github.quickmsg.common.cluster.ClusterConfig;
import io.github.quickmsg.common.config.SslContext;
import io.github.quickmsg.common.transport.Transport;
import io.github.quickmsg.core.http.HttpConfiguration;
import io.github.quickmsg.core.http.HttpTransportFactory;
import io.github.quickmsg.core.mqtt.MqttConfiguration;
import io.github.quickmsg.core.mqtt.MqttTransportFactory;
import io.github.quickmsg.core.websocket.WebSocketMqttTransportFactory;
import io.netty.channel.ChannelOption;
import io.netty.channel.WriteBufferWaterMark;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author luxurong
 */
@Builder
@Getter
@ToString
@Slf4j
public class Bootstrap {

    private static final Sinks.One<Void> START_ONLY_MQTT = Sinks.one();

    private static final Integer DEFAULT_WEBSOCKET_MQTT_PORT = 9997;


    @Builder.Default
    private Boolean isWebsocket = false;

    @Builder.Default
    private Integer websocketPort = 0;

    private final List<Transport<?>> transports = new ArrayList<>();

    private Integer port;

    private Integer lowWaterMark;

    private Integer highWaterMark;

    private Boolean ssl;

    private SslContext sslContext;

    private Boolean wiretap;

    private Integer bossThreadSize;

    private Integer workThreadSize;

    private HttpOptions httpOptions;

    private String host;

    private PasswordAuthentication reactivePasswordAuth;

    private Consumer<Map<ChannelOption<?>, ?>> options;

    private Consumer<Map<ChannelOption<?>, ?>> childOptions;

    private ClusterConfig clusterConfig;


    private MqttConfiguration initMqttConfiguration() {
        MqttConfiguration mqttConfiguration = defaultConfiguration();
        Optional.ofNullable(options).ifPresent(mqttConfiguration::setOptions);
        Optional.ofNullable(childOptions).ifPresent(mqttConfiguration::setChildOptions);
        Optional.ofNullable(reactivePasswordAuth).ifPresent(mqttConfiguration::setReactivePasswordAuth);
        Optional.ofNullable(port).ifPresent(mqttConfiguration::setPort);
        Optional.ofNullable(lowWaterMark).ifPresent(mqttConfiguration::setLowWaterMark);
        Optional.ofNullable(highWaterMark).ifPresent(mqttConfiguration::setHighWaterMark);
        Optional.ofNullable(wiretap).ifPresent(mqttConfiguration::setWiretap);
        Optional.ofNullable(bossThreadSize).ifPresent(mqttConfiguration::setBossThreadSize);
        Optional.ofNullable(workThreadSize).ifPresent(mqttConfiguration::setWorkThreadSize);
        Optional.ofNullable(ssl).ifPresent(mqttConfiguration::setSsl);
        Optional.ofNullable(sslContext).ifPresent(mqttConfiguration::setSslContext);
        Optional.ofNullable(clusterConfig).ifPresent(mqttConfiguration::setClusterConfig);
        if (isWebsocket) {
            mqttConfiguration.setWebSocketPort(websocketPort);
        }
        return mqttConfiguration;
    }


    private MqttConfiguration defaultConfiguration() {
        MqttConfiguration mqttConfiguration = new MqttConfiguration();
        mqttConfiguration.setLowWaterMark(WriteBufferWaterMark.DEFAULT.low());
        mqttConfiguration.setHighWaterMark(WriteBufferWaterMark.DEFAULT.high());
        return mqttConfiguration;
    }


    /**
     * 阻塞启动 生产环境慎用
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
    public Mono<Bootstrap> start() {
        MqttConfiguration mqttConfiguration = initMqttConfiguration();
        MqttTransportFactory mqttTransportFactory = new MqttTransportFactory();
        return mqttTransportFactory.createTransport(mqttConfiguration)
                .start()
                .doOnError(Throwable::printStackTrace)
                .doOnSuccess(transports::add)
                .then(startWs(mqttConfiguration))
                .then(startHttp())
                .thenReturn(this);
    }


    private Mono<Void> startWs(MqttConfiguration mqttConfiguration) {
        return this.isWebsocket ? new WebSocketMqttTransportFactory().createTransport(mqttConfiguration)
                .start()
                .doOnSuccess(transports::add).then() : Mono.empty();
    }


    private Mono<Void> startHttp() {
        return httpOptions != null ? new HttpTransportFactory().createTransport(this.buildHttpConfiguration())
                .start()
                .doOnSuccess(transports::add).then() : Mono.empty();
    }


    private HttpConfiguration buildHttpConfiguration() {
        HttpConfiguration httpConfiguration = new HttpConfiguration();
        Optional.ofNullable(this.httpOptions.accessLog).ifPresent(httpConfiguration::setAccessLog);
        Optional.ofNullable(this.httpOptions.sslContext).ifPresent(httpConfiguration::setSslContext);
        Optional.ofNullable(this.httpOptions.httpPort).ifPresent(httpConfiguration::setPort);
        return httpConfiguration;
    }

    public void shutdown() {
        transports.forEach(Transport::dispose);
    }

    @Getter
    @Builder
    public static class HttpOptions {

        @Builder.Default
        private Integer httpPort = 0;

        @Builder.Default
        private Boolean ssl = false;

        private SslContext sslContext;

        @Builder.Default
        private Boolean accessLog = false;

    }



}
