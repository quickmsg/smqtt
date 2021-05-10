package io.github.quickmsg;

import io.github.quickmsg.common.bootstrap.BootstrapKey;
import io.github.quickmsg.common.cluster.ClusterConfig;
import io.github.quickmsg.common.config.SslContext;
import io.github.quickmsg.common.utils.PropertiesLoader;
import io.github.quickmsg.core.Bootstrap;
import io.netty.channel.WriteBufferWaterMark;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

/**
 * @author luxurong
 */
public abstract class AbstractStarter {

    private static final String DEFAULT_PROPERTIES_LOAD_CONFIG_PATH = "/conf/config.properties";

    private static final Integer DEFAULT_MQTT_PORT = 1883;

    private static final Integer DEFAULT_WEBSOCKET_MQTT_PORT = 8999;

    private static final Integer DEFAULT_CLUSTER_PORT = 4333;


    private static final Integer DEFAULT_HTTP_PORT = 12000;


    private static final String DEFAULT_AUTH_USERNAME_PASSWORD = "smqtt";

    public static void start(Function<String, String> function) {
        start(function, null);
    }


    public static void start(Function<String, String> function, String path) {
        path = path == null ? DEFAULT_PROPERTIES_LOAD_CONFIG_PATH : path;
        Map<String, String> params = PropertiesLoader.loadProperties(path);

        Integer port = Optional.ofNullable(params.getOrDefault(BootstrapKey.BOOTSTRAP_PORT, function.apply(BootstrapKey.BOOTSTRAP_PORT)))
                .map(Integer::parseInt).orElse(DEFAULT_MQTT_PORT);

        Integer lowWaterMark = Optional.ofNullable(params.getOrDefault(BootstrapKey.BOOTSTRAP_LOW_WATERMARK, function.apply(BootstrapKey.BOOTSTRAP_LOW_WATERMARK)))
                .map(Integer::parseInt).orElse(WriteBufferWaterMark.DEFAULT.low());

        Integer highWaterMark = Optional.ofNullable(params.getOrDefault(BootstrapKey.BOOTSTRAP_HIGH_WATERMARK, function.apply(BootstrapKey.BOOTSTRAP_HIGH_WATERMARK)))
                .map(Integer::parseInt).orElse(WriteBufferWaterMark.DEFAULT.high());

        Boolean wiretap = Optional.ofNullable(params.getOrDefault(BootstrapKey.BOOTSTRAP_WIRETAP, function.apply(BootstrapKey.BOOTSTRAP_WIRETAP)))
                .map(Boolean::parseBoolean).orElse(false);

        Integer bossThreadSize = Optional.ofNullable(params.getOrDefault(BootstrapKey.BOOTSTRAP_BOSS_THREAD_SIZE, function.apply(BootstrapKey.BOOTSTRAP_BOSS_THREAD_SIZE)))
                .map(Integer::parseInt).orElse(Runtime.getRuntime().availableProcessors() >> 1);

        Integer workThreadSize = Optional.ofNullable(params.getOrDefault(BootstrapKey.BOOTSTRAP_WORK_THREAD_SIZE, function.apply(BootstrapKey.BOOTSTRAP_WORK_THREAD_SIZE)))
                .map(Integer::parseInt).orElse(Runtime.getRuntime().availableProcessors());

        Boolean isWebsocket = Optional.ofNullable(params.getOrDefault(BootstrapKey.BOOTSTRAP_WEB_SOCKET_ENABLE, function.apply(BootstrapKey.BOOTSTRAP_WEB_SOCKET_ENABLE)))
                .map(Boolean::parseBoolean).orElse(false);

        Boolean ssl = Optional.ofNullable(params.getOrDefault(BootstrapKey.BOOTSTRAP_SSL, function.apply(BootstrapKey.BOOTSTRAP_SSL)))
                .map(Boolean::parseBoolean).orElse(false);

        String username = Optional.ofNullable(params.getOrDefault(BootstrapKey.BOOTSTRAP_USERNAME, function.apply(BootstrapKey.BOOTSTRAP_USERNAME)))
                .map(String::valueOf).orElse(DEFAULT_AUTH_USERNAME_PASSWORD);

        String password = Optional.ofNullable(params.getOrDefault(BootstrapKey.BOOTSTRAP_PASSWORD, function.apply(BootstrapKey.BOOTSTRAP_PASSWORD)))
                .map(String::valueOf).orElse(DEFAULT_AUTH_USERNAME_PASSWORD);


        Boolean httpEnable = Optional.ofNullable(params.getOrDefault(BootstrapKey.BOOTSTRAP_HTTP_ENABLE, function.apply(BootstrapKey.BOOTSTRAP_HTTP_ENABLE)))
                .map(Boolean::parseBoolean).orElse(false);

        Boolean clusterEnable = Optional.ofNullable(params.getOrDefault(BootstrapKey.BOOTSTRAP_CLUSTER_ENABLE, function.apply(BootstrapKey.BOOTSTRAP_CLUSTER_ENABLE)))
                .map(Boolean::parseBoolean).orElse(false);


        Bootstrap.BootstrapBuilder builder = Bootstrap.builder();
        builder.port(port)
                .reactivePasswordAuth(((userName, passwordInBytes) -> userName.equals(username) && password.equals(new String(passwordInBytes))))
                .bossThreadSize(bossThreadSize)
                .wiretap(wiretap)
                .ssl(ssl)
                .workThreadSize(workThreadSize)
                .lowWaterMark(lowWaterMark)
                .highWaterMark(highWaterMark);
        if (ssl) {
            String sslCrt = Optional.ofNullable(params.getOrDefault(BootstrapKey.BOOTSTRAP_SSL_CRT, function.apply(BootstrapKey.BOOTSTRAP_SSL_CRT)))
                    .map(String::valueOf).orElse(null);
            String sslKey = Optional.ofNullable(params.getOrDefault(BootstrapKey.BOOTSTRAP_SSL_KEY, function.apply(BootstrapKey.BOOTSTRAP_SSL_KEY)))
                    .map(String::valueOf).orElse(null);
            if (sslCrt == null || sslKey == null) {
                builder.sslContext(null);
            } else {
                builder.sslContext(new SslContext(sslCrt, sslKey));
            }
        }
        if (clusterEnable) {
            Integer clusterPort = Optional.ofNullable(params.getOrDefault(BootstrapKey.BOOTSTRAP_CLUSTER_PORT, function.apply(BootstrapKey.BOOTSTRAP_CLUSTER_PORT)))
                    .map(Integer::parseInt).orElse(DEFAULT_CLUSTER_PORT);
            String clusterUrl = Optional.ofNullable(params.getOrDefault(BootstrapKey.BOOTSTRAP_CLUSTER_URL, function.apply(BootstrapKey.BOOTSTRAP_CLUSTER_URL)))
                    .map(String::valueOf).orElse(null);
            String clusterNode = Optional.ofNullable(params.getOrDefault(BootstrapKey.BOOTSTRAP_CLUSTER_NODE, function.apply(BootstrapKey.BOOTSTRAP_CLUSTER_NODE)))
                    .map(String::valueOf).orElse(UUID.randomUUID().toString().replaceAll("-", ""));
            ClusterConfig clusterConfig =
                    ClusterConfig.builder()
                            .port(clusterPort)
                            .clusterUrl(clusterUrl)
                            .nodeName(clusterNode)
                            .clustered(true)
                            .build();
            builder.clusterConfig(clusterConfig);
        }
        if (isWebsocket) {
            Integer websocketPort = Optional.ofNullable(params.getOrDefault(BootstrapKey.BOOTSTRAP_WEB_SOCKET_PORT, function.apply(BootstrapKey.BOOTSTRAP_WEB_SOCKET_PORT)))
                    .map(Integer::parseInt).orElse(DEFAULT_WEBSOCKET_MQTT_PORT);
            builder.isWebsocket(true)
                    .websocketPort(websocketPort);
        }
        if (httpEnable) {
            Bootstrap.HttpOptions.HttpOptionsBuilder optionsBuilder = Bootstrap.HttpOptions.builder();

            Integer httpPort = Optional.ofNullable(params.getOrDefault(BootstrapKey.BOOTSTRAP_HTTP_PORT, function.apply(BootstrapKey.BOOTSTRAP_HTTP_PORT)))
                    .map(Integer::parseInt).orElse(DEFAULT_HTTP_PORT);
            Boolean accessLog = Optional.ofNullable(params.getOrDefault(BootstrapKey.BOOTSTRAP_HTTP_ACCESS_LOG, function.apply(BootstrapKey.BOOTSTRAP_HTTP_ACCESS_LOG)))
                    .map(Boolean::parseBoolean).orElse(false);


            Boolean httpSsl = Optional.ofNullable(params.getOrDefault(BootstrapKey.BOOTSTRAP_HTTP_SSL_ENABLE, function.apply(BootstrapKey.BOOTSTRAP_HTTP_SSL_ENABLE)))
                    .map(Boolean::parseBoolean).orElse(false);

            if (httpSsl) {
                String httpSslCrt = Optional.ofNullable(params.getOrDefault(BootstrapKey.BOOTSTRAP_HTTP_SSL_CRT, function.apply(BootstrapKey.BOOTSTRAP_HTTP_SSL_CRT)))
                        .map(String::valueOf).orElse(null);
                String httpSslKey = Optional.ofNullable(params.getOrDefault(BootstrapKey.BOOTSTRAP_HTTP_SSL_KEY, function.apply(BootstrapKey.BOOTSTRAP_HTTP_SSL_KEY)))
                        .map(String::valueOf).orElse(null);
                if (httpSslKey == null || httpSslCrt == null) {
                    optionsBuilder.sslContext(null);
                } else {
                    optionsBuilder.sslContext(new SslContext(httpSslCrt, httpSslKey));
                }
            }
            optionsBuilder.httpPort(httpPort)
                    .accessLog(accessLog)
                    .ssl(httpSsl);
            Bootstrap.HttpOptions options = optionsBuilder.build();
            builder.httpOptions(options);
        }
        builder.build().startAwait();
    }

}
