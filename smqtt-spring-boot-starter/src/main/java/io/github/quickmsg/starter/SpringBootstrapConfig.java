package io.github.quickmsg.starter;

import ch.qos.logback.classic.Level;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.quickmsg.common.config.AclConfig;
import io.github.quickmsg.common.config.BootstrapConfig;
import io.github.quickmsg.common.rule.RuleChainDefinition;
import io.github.quickmsg.common.rule.source.SourceDefinition;
import io.github.quickmsg.core.Bootstrap;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author luxurong
 */

@Configuration
@ConfigurationProperties(prefix = "smqtt")
@Getter
@Setter
@ToString
@EnableAutoConfiguration
@Component
public class SpringBootstrapConfig {

    /**
     * sfl4j日志级别
     *
     * @see Level
     */
    private String logLevel;

    /**
     * tcp配置
     */
    private BootstrapConfig.TcpConfig tcp;

    /**
     * http配置
     */
    private BootstrapConfig.HttpConfig http;

    /**
     * websocket配置
     */
    private BootstrapConfig.WebsocketConfig ws;

    /**
     * 集群配置配置
     */
    private BootstrapConfig.ClusterConfig cluster;

    /**
     * meter配置
     */
    private BootstrapConfig.MeterConfig meter;


    /**
     * 数据库配置
     */
    private BootstrapConfig.DatabaseConfig db;
    /**
     * redis配置
     */
    private BootstrapConfig.RedisConfig redis;
    /**
     * 规则定义
     */
    private List<RuleChainDefinition> rules;

    /**
     * 规则定义
     */
    private List<SourceDefinition> sources;

    /**
     * acl
     */
    private AclConfig acl;

}
