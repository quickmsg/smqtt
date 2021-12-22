package io.github.quickmsg.starter;

import ch.qos.logback.classic.Level;
import io.github.quickmsg.common.auth.PasswordAuthentication;
import io.github.quickmsg.common.config.ConnectModel;
import io.github.quickmsg.common.utils.IPUtils;
import io.github.quickmsg.core.Bootstrap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author luxurong
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(SpringBootstrapConfig.class)
public class AutoMqttConfiguration  {


    /**
     * 配置异常切面
     *
     * @param springBootstrapConfig {@link SpringBootstrapConfig}
     * @return {@link Bootstrap}
     */
    @Bean
    public Bootstrap startServer(@Autowired SpringBootstrapConfig springBootstrapConfig, @Autowired(required = false) PasswordAuthentication authentication) {
        check(springBootstrapConfig,authentication);
        return Bootstrap.builder()
                .rootLevel(Level.toLevel(springBootstrapConfig.getLogLevel()))
                .tcpConfig(springBootstrapConfig.getTcp())
                .httpConfig(springBootstrapConfig.getHttp())
                .websocketConfig(springBootstrapConfig.getWs())
                .clusterConfig(springBootstrapConfig.getCluster())
                .redisConfig(springBootstrapConfig.getRedis())
                .databaseConfig(springBootstrapConfig.getDb())
                .ruleChainDefinitions(springBootstrapConfig.getRules())
                .sourceDefinitions(springBootstrapConfig.getSources())
                .meterConfig(springBootstrapConfig.getMeter())
                .build()
                .start()
                .doOnSuccess(this::printUiUrl).block();
    }

    private void check(SpringBootstrapConfig springBootstrapConfig, PasswordAuthentication authentication) {
        if(springBootstrapConfig.getTcp().getConnectModel() == null){
            springBootstrapConfig.getTcp().setConnectModel(ConnectModel.UNIQUE);
        }
        if(authentication !=null){
            springBootstrapConfig.getTcp().setAuthentication(authentication);
        }
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
