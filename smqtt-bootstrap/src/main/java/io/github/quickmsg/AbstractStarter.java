package io.github.quickmsg;

import ch.qos.logback.classic.Level;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.github.quickmsg.common.cluster.ClusterConfig;
import io.github.quickmsg.common.config.BootstrapConfig;
import io.github.quickmsg.common.config.SslContext;
import io.github.quickmsg.common.utils.IPUtils;
import io.github.quickmsg.core.Bootstrap;
import io.github.quickmsg.exception.NotSupportConfigException;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Objects;
import java.util.Optional;

/**
 * @author luxurong
 */
@Slf4j
public abstract class AbstractStarter {


    private static final String PROPERTIES_SYMBOL = ".properties";

    private static final String YAML_SYMBOL_1 = ".yaml";

    private static final String YAML_SYMBOL_2 = ".yml";


    public static void start(String path) {
        BootstrapConfig config = null;
        if (path != null) {
            if (path.endsWith(PROPERTIES_SYMBOL)) {
                ObjectMapper mapper = new ObjectMapper(new JavaPropsFactory());
                try {
                    config = mapper.readValue(new File(path), BootstrapConfig.class);
                } catch (Exception e) {
                    log.error("properties read error", e);
                }
            } else if (path.endsWith(YAML_SYMBOL_1) || path.endsWith(YAML_SYMBOL_2)) {
                ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
                try {
                    config = mapper.readValue(new File(path), BootstrapConfig.class);
                } catch (Exception e) {
                    log.error("yaml read error", e);
                }
            } else {
                throw new NotSupportConfigException();
            }
        }
        if (config == null) {
            config = BootstrapConfig.defaultConfig();
        }

        Bootstrap.BootstrapBuilder builder = Bootstrap.builder();
        // mqtt设置
        BootstrapConfig.TcpConfig tcpConfig = config.getSmqttConfig().getTcpConfig();
        builder.port(tcpConfig.getPort())
                .reactivePasswordAuth(((userName, passwordInBytes) ->
                        !Objects.isNull(userName) && !Objects.isNull(passwordInBytes) && (userName.equals(tcpConfig.getUsername()) && tcpConfig.getPassword().equals(new String(passwordInBytes)))))
                .bossThreadSize(tcpConfig.getBossThreadSize())
                .wiretap(tcpConfig.getWiretap())
                .sslContext(tcpConfig.getSslContext())
                .workThreadSize(tcpConfig.getWorkThreadSize())
                .lowWaterMark(tcpConfig.getLowWaterMark())
                .bootstrapConfig(config)
                .highWaterMark(tcpConfig.getHighWaterMark());
        if (config.getSmqttConfig().getLogLevel() != null) {
            builder.rootLevel(Level.toLevel(config.getSmqttConfig().getLogLevel()));
        }
        // 集群设置
        BootstrapConfig.ClusterConfig clusterConfig = config.getSmqttConfig().getClusterConfig();
        ClusterConfig sClusterConfig = Optional.ofNullable(clusterConfig)
                .map(cf -> ClusterConfig.builder()
                        .port(cf.getPort())
                        .clusterUrl(cf.getUrl())
                        .nodeName(cf.getNode())
                        .externalPort(cf.getExternal().getPort())
                        .externalHost(cf.getExternal().getHost())
                        .clustered(cf.isEnable())
                        .build())
                .orElse(ClusterConfig.builder().clustered(false).build());
        builder.clusterConfig(sClusterConfig);

        // ws设置
        BootstrapConfig.WebsocketConfig websocketConfig = config.getSmqttConfig().getWebsocketConfig();
        if (websocketConfig == null) {
            builder.isWebsocket(false);
        } else {
            builder.isWebsocket(websocketConfig.isEnable())
                    .websocketPort(websocketConfig.getPort()).websocketPath(websocketConfig.getPath());
        }

        // http设置
        BootstrapConfig.HttpConfig httpConfig = config.getSmqttConfig().getHttpConfig();
        Bootstrap.HttpOptions.HttpOptionsBuilder optionsBuilder = Bootstrap.HttpOptions.builder();
        if (httpConfig != null) {
            if (httpConfig.isEnable()) {
                if (httpConfig.getHttpAdmin() != null) {
                    optionsBuilder.enableAdmin(httpConfig.getHttpAdmin().isEnable());
                    optionsBuilder.username(httpConfig.getHttpAdmin().getUsername()).password(httpConfig.getHttpAdmin().getPassword());
                } else {
                    optionsBuilder.enableAdmin(false);
                }
                optionsBuilder
                        .accessLog(httpConfig.isAccessLog())
                        .ssl(Optional.ofNullable(httpConfig.getSslContext()).map(SslContext::getEnable).orElse(false))
                        .sslContext(httpConfig.getSslContext());
                Bootstrap.HttpOptions options = optionsBuilder.build();
                builder.httpOptions(options);
            }
        }
        Bootstrap bootstrap = builder.build();
        bootstrap.doOnStarted(AbstractStarter::printUiUrl).startAwait();
    }

    /**
     * 打印前端访问地址
     *
     * @param bootstrap 启动类
     */
    public static void printUiUrl(Bootstrap bootstrap) {
        String start = "\n-------------------------------------------------------------\n\t";
        start += String.format("Smqtt mqtt connect url %s:%s \n\t", IPUtils.getIP(), bootstrap.getPort());
        if (bootstrap.getHttpOptions() != null && bootstrap.getHttpOptions().getEnableAdmin()) {
            Integer port = bootstrap.getHttpOptions().getHttpPort();
            start += String.format("Smqtt-Admin UI is running AccessURLs:\n\t" +
                    "Http Local url:    http://localhost:%s/smqtt/admin" + "\n\t" +
                    "Http External url: http://%s:%s/smqtt/admin" + "\n" +
                    "-------------------------------------------------------------", port, IPUtils.getIP(), port);
        }
        log.info(start);
    }
}
