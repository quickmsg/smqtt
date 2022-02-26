package io.github.quickmsg.core;

import ch.qos.logback.classic.Level;
import io.github.quickmsg.common.config.AclConfig;
import io.github.quickmsg.common.config.BootstrapConfig;
import io.github.quickmsg.common.config.SslContext;
import io.github.quickmsg.common.rule.RuleChainDefinition;
import io.github.quickmsg.common.rule.source.SourceDefinition;
import io.github.quickmsg.common.transport.Transport;
import io.github.quickmsg.common.utils.BannerUtils;
import io.github.quickmsg.common.utils.LoggerLevel;
import io.github.quickmsg.core.http.HttpConfiguration;
import io.github.quickmsg.core.http.HttpTransportFactory;
import io.github.quickmsg.core.mqtt.MqttConfiguration;
import io.github.quickmsg.core.mqtt.MqttTransportFactory;
import io.github.quickmsg.core.websocket.WebSocketMqttTransportFactory;
import io.netty.channel.WriteBufferWaterMark;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.*;
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

    private BootstrapConfig.TcpConfig tcpConfig;

    private BootstrapConfig.HttpConfig httpConfig;

    private BootstrapConfig.WebsocketConfig websocketConfig;

    private BootstrapConfig.ClusterConfig clusterConfig;

    private BootstrapConfig.RedisConfig redisConfig;

    private BootstrapConfig.DatabaseConfig databaseConfig;

    private BootstrapConfig.MeterConfig meterConfig;

    private List<RuleChainDefinition> ruleChainDefinitions;

    private List<SourceDefinition> sourceDefinitions;

    private AclConfig aclConfig;

    private final List<Transport<?>> transports = new ArrayList<>();

    @Builder.Default
    private Consumer<Bootstrap> started = bootstrap -> {
    };

    @Builder.Default
    private Level rootLevel = Level.INFO;


    @SuppressWarnings("Unchecked")
    private MqttConfiguration initMqttConfiguration() {

        MqttConfiguration mqttConfiguration = defaultConfiguration();
        Optional.ofNullable(tcpConfig.getConnectModel()).ifPresent(mqttConfiguration::setConnectModel);
        Optional.ofNullable(tcpConfig.getNotKickSecond()).ifPresent(mqttConfiguration::setNotKickSecond);
        Optional.ofNullable(tcpConfig.getPort()).ifPresent(mqttConfiguration::setPort);
        Optional.ofNullable(tcpConfig.getLowWaterMark()).ifPresent(mqttConfiguration::setLowWaterMark);
        Optional.ofNullable(tcpConfig.getHighWaterMark()).ifPresent(mqttConfiguration::setHighWaterMark);
        Optional.ofNullable(tcpConfig.getWiretap()).ifPresent(mqttConfiguration::setWiretap);
        Optional.ofNullable(tcpConfig.getBossThreadSize()).ifPresent(mqttConfiguration::setBossThreadSize);
        Optional.ofNullable(tcpConfig.getWorkThreadSize()).ifPresent(mqttConfiguration::setWorkThreadSize);
        Optional.ofNullable(tcpConfig.getBusinessThreadSize()).ifPresent(mqttConfiguration::setBusinessThreadSize);
        Optional.ofNullable(tcpConfig.getBusinessQueueSize()).ifPresent(mqttConfiguration::setBusinessQueueSize);
        Optional.ofNullable(tcpConfig.getChannelReadWriteSize()).ifPresent(mqttConfiguration::setChannelReadWriteSize);
        Optional.ofNullable(tcpConfig.getGlobalReadWriteSize()).ifPresent(mqttConfiguration::setGlobalReadWriteSize);
        Optional.ofNullable(tcpConfig.getSsl()).map(SslContext::getEnable).ifPresent(mqttConfiguration::setSsl);
        Optional.ofNullable(tcpConfig.getSsl()).ifPresent(mqttConfiguration::setSslContext);
        Optional.ofNullable(tcpConfig.getSsl()).ifPresent(mqttConfiguration::setSslContext);
        Optional.ofNullable(tcpConfig.getMessageMaxSize()).ifPresent(mqttConfiguration::setMessageMaxSize);
        Optional.ofNullable(clusterConfig).ifPresent(mqttConfiguration::setClusterConfig);
        Optional.ofNullable(meterConfig).ifPresent(mqttConfiguration::setMeterConfig);
        Optional.ofNullable(aclConfig).ifPresent(mqttConfiguration::setAclConfig);

        if (websocketConfig != null && websocketConfig.isEnable()) {
            mqttConfiguration.setWebSocketPort(websocketConfig.getPort());
            mqttConfiguration.setWebSocketPath(websocketConfig.getPath());
        }
        if (tcpConfig.getWiretap() != null && tcpConfig.getWiretap()) {
            LoggerLevel.wiretap();
        }
        mqttConfiguration.setOptions(tcpConfig.getOptions());
        mqttConfiguration.setChildOptions(tcpConfig.getChildOptions());
        mqttConfiguration.setRuleChainDefinitions(ruleChainDefinitions);
        mqttConfiguration.setSourceDefinitions(sourceDefinitions);
        Map<Object, Object> environmentMap = new HashMap<>();
        environmentMap.put(BootstrapConfig.RedisConfig.class, this.redisConfig);
        environmentMap.put(BootstrapConfig.DatabaseConfig.class, this.databaseConfig);
        mqttConfiguration.setEnvironmentMap(environmentMap);
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
        BannerUtils.banner();
        MqttConfiguration mqttConfiguration = initMqttConfiguration();
        MqttTransportFactory mqttTransportFactory = new MqttTransportFactory();
        LoggerLevel.root(rootLevel);

        return mqttTransportFactory.createTransport(mqttConfiguration)
                .start()
                .doOnError(Throwable::printStackTrace)
                .doOnSuccess(transports::add)
                .then(startWs(mqttConfiguration))
                .then(startHttp())
                .thenReturn(this)
                .doOnSuccess(started);
    }


    private Mono<Void> startWs(MqttConfiguration mqttConfiguration) {
        return this.websocketConfig != null && websocketConfig.isEnable() ? new WebSocketMqttTransportFactory().createTransport(mqttConfiguration)
                .start()
                .doOnSuccess(transports::add).doOnError(throwable -> log.error("start websocket error", throwable)).then() : Mono.empty();
    }


    private Mono<Void> startHttp() {
        return httpConfig != null && httpConfig.isEnable() ? new HttpTransportFactory().createTransport(this.buildHttpConfiguration())
                .start()
                .doOnSuccess(transports::add).doOnError(throwable -> log.error("start http error", throwable)).then() : Mono.empty();
    }

    private HttpConfiguration buildHttpConfiguration() {
        HttpConfiguration httpConfiguration = new HttpConfiguration();
        httpConfiguration.setAccessLog(this.httpConfig.isAccessLog());
        httpConfiguration.setSslContext(this.httpConfig.getSsl());
        BootstrapConfig.HttpAdmin httpAdmin = this.httpConfig.getAdmin();
        if (httpAdmin != null && httpAdmin.isEnable()) {
            httpConfiguration.setEnableAdmin(true);
            httpConfiguration.setUsername(httpAdmin.getUsername());
            httpConfiguration.setPassword(httpAdmin.getPassword());

        } else {
            httpConfiguration.setEnableAdmin(false);
        }
        return httpConfiguration;
    }

    public void shutdown() {
        transports.forEach(Transport::dispose);
    }

    @Getter
    @Builder
    public static class HttpOptions {

        private final Integer httpPort = 60000;

        @Builder.Default
        private Boolean ssl = false;

        private SslContext sslContext;

        @Builder.Default
        private Boolean accessLog = false;

        private Boolean enableAdmin;

        private String username;

        private String password;

    }

    public Bootstrap doOnStarted(Consumer<Bootstrap> started) {
        this.started = started;
        return this;
    }


}
