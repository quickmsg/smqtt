package io.github.quickmsg.core.mqtt;

import io.github.quickmsg.common.auth.PasswordAuthentication;
import io.github.quickmsg.common.config.AbstractConfiguration;
import io.github.quickmsg.common.config.BootstrapConfig;
import io.github.quickmsg.common.config.ConnectModel;
import io.github.quickmsg.common.config.SslContext;
import io.github.quickmsg.common.rule.RuleChainDefinition;
import io.github.quickmsg.common.rule.source.SourceDefinition;
import io.github.quickmsg.core.ssl.AbstractSslHandler;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author luxurong
 */
@Data
public class MqttConfiguration extends AbstractSslHandler implements AbstractConfiguration {

    private Integer port = 1883;

    private Integer webSocketPort = 0;

    private String webSocketPath = "/";

    private Integer lowWaterMark;

    private Integer highWaterMark;

    private Boolean wiretap = false;

    private Boolean ssl = false;

    private SslContext sslContext;

    private ConnectModel connectModel;

    private PasswordAuthentication reactivePasswordAuth = (u, p, c) -> true;

    private Integer bossThreadSize = Runtime.getRuntime().availableProcessors();

    private Integer workThreadSize = Runtime.getRuntime().availableProcessors() * 2;

    private Integer businessThreadSize = Runtime.getRuntime().availableProcessors() * 4;

    private Integer businessQueueSize = 100000;

    private String globalReadWriteSize;

    private String channelReadWriteSize;

    private Map<String, Object> options;

    private Map<String, Object> childOptions;

    private BootstrapConfig.ClusterConfig clusterConfig;

    private BootstrapConfig.MeterConfig meterConfig ;

    private List<RuleChainDefinition> ruleChainDefinitions;

    private List<SourceDefinition> sourceDefinitions;

    private Map<Object, Object> environmentMap;

    private Integer messageMaxSize = 4194304;

}
