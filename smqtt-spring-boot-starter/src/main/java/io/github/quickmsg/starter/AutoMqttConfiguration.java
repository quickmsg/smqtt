package io.github.quickmsg.starter;

import ch.qos.logback.classic.Level;
import io.github.quickmsg.common.utils.IPUtils;
import io.github.quickmsg.core.Bootstrap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author luxurong
 * @date 2021/9/17 17:22
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(SpringBootstrapConfig.class)
public class AutoMqttConfiguration {


    /**
     * 配置异常切面
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "smqtt", name = "enable", havingValue = "true")
    @ConditionalOnBean(SpringBootstrapConfig.class)
    public void startServer(@Autowired SpringBootstrapConfig springBootstrapConfig) {
        Bootstrap.builder()
                .rootLevel(Level.toLevel(springBootstrapConfig.getLogLevel()))
                .tcpConfig(springBootstrapConfig.getTcpConfig())
                .httpConfig(springBootstrapConfig.getHttpConfig())
                .websocketConfig(springBootstrapConfig.getWebsocketConfig())
                .clusterConfig(springBootstrapConfig.getClusterConfig())
                .redisConfig(springBootstrapConfig.getRedisConfig())
                .databaseConfig(springBootstrapConfig.getDatabaseConfig())
                .ruleChainDefinitions(springBootstrapConfig.getRuleChainDefinitions())
                .sourceDefinitions(springBootstrapConfig.getRuleSources())
                .build()
                .doOnStarted(this::printUiUrl).startAwait();

    }

    public void printUiUrl(Bootstrap bootstrap) {
        String start = "\n-------------------------------------------------------------\n\t";
        start += String.format("Smqtt mqtt connect url %s:%s \n\t", IPUtils.getIP(), bootstrap.getTcpConfig().getPort());
        if (bootstrap.getHttpConfig() != null && bootstrap.getHttpConfig().isEnable()) {
            Integer port = 60000;
            start += String.format("Smqtt-Admin UI is running AccessURLs:\n\t" +
                    "Http Local url:    http://localhost:%s/smqtt/admin" + "\n\t" +
                    "Http External url: http://%s:%s/smqtt/admin" + "\n" +
                    "-------------------------------------------------------------", port, IPUtils.getIP(), port);
        }
        log.info(start);
    }


}
