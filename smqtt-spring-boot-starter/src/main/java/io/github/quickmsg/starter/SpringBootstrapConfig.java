package io.github.quickmsg.starter;

import ch.qos.logback.classic.Level;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.quickmsg.common.config.BootstrapConfig;
import io.github.quickmsg.common.rule.RuleChainDefinition;
import io.github.quickmsg.common.rule.source.SourceDefinition;
import io.github.quickmsg.core.Bootstrap;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author luxurong
 * @date 2021/9/17 17:27
 */

@Configuration
@ConfigurationProperties(prefix = "smqtt")
@Getter
@Setter
@ToString
public class SpringBootstrapConfig {

    /**
     * 是否开启
     */
    private boolean enable;

    /**
     * sfl4j日志级别
     *
     * @see Level
     */
    private String logLevel;

    /**
     * tcp配置
     */
    @JsonProperty("tcp")
    private BootstrapConfig.TcpConfig tcpConfig;

    /**
     * http配置
     */
    @JsonProperty("http")
    private BootstrapConfig.HttpConfig httpConfig;

    /**
     * websocket配置
     */
    @JsonProperty("ws")
    private BootstrapConfig.WebsocketConfig websocketConfig;

    /**
     * 集群配置配置
     */
    @JsonProperty("cluster")
    private BootstrapConfig.ClusterConfig clusterConfig;


    /**
     * 数据库配置
     */
    @JsonProperty("db")
    private BootstrapConfig.DatabaseConfig databaseConfig;
    /**
     * redis配置
     */
    @JsonProperty("redis")
    private BootstrapConfig.RedisConfig redisConfig;
    /**
     * 规则定义
     */
    @JsonProperty("rules")
    private List<RuleChainDefinition> ruleChainDefinitions;

    /**
     * 规则定义
     */
    @JsonProperty("sources")
    private List<SourceDefinition> ruleSources;


}
