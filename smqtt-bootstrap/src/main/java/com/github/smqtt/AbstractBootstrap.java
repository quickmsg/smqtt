package com.github.smqtt;

import io.netty.channel.WriteBufferWaterMark;

import java.util.Optional;
import java.util.function.Function;

/**
 * @author luxurong
 * @date 2021/4/15 14:14
 * @description
 */
public abstract class AbstractBootstrap {

    private static final Integer DEFAULT_MQTT_PORT = 1883;

    private static final Integer DEFAULT_WEBSOCKET_MQTT_PORT = 8999;

    private static final String DEFAULT_AUTH_USERNAME_PASSWORD = "smqtt";


    public static void bootstrap(Function<String, String> function) {

        Integer port = Optional.ofNullable(function.apply(BootstrapKey.BOOTSTRAP_PORT))
                .map(Integer::parseInt).orElse(DEFAULT_MQTT_PORT);
        Integer lowWaterMark = Optional.ofNullable(function.apply(BootstrapKey.BOOTSTRAP_LOW_WATERMARK))
                .map(Integer::parseInt).orElse(WriteBufferWaterMark.DEFAULT.low());
        Integer highWaterMark = Optional.ofNullable(function.apply(BootstrapKey.BOOTSTRAP_HIGH_WATERMARK))
                .map(Integer::parseInt).orElse(WriteBufferWaterMark.DEFAULT.high());
        Boolean wiretap = Optional.ofNullable(function.apply(BootstrapKey.BOOTSTRAP_WIRETAP))
                .map(Boolean::parseBoolean).orElse(false);
        Integer bossThreadSize = Optional.ofNullable(function.apply(BootstrapKey.BOOTSTRAP_BOSS_THREAD_SIZE))
                .map(Integer::parseInt).orElse(Runtime.getRuntime().availableProcessors() >> 1);
        Integer workThreadSize = Optional.ofNullable(function.apply(BootstrapKey.BOOTSTRAP_WORK_THREAD_SIZE))
                .map(Integer::parseInt).orElse(Runtime.getRuntime().availableProcessors());

        Boolean isWebsocket = Optional.ofNullable(function.apply(BootstrapKey.BOOTSTRAP_WEB_SOCKET_ENABLE))
                .map(Boolean::parseBoolean).orElse(false);
        Boolean ssl = Optional.ofNullable(function.apply(BootstrapKey.BOOTSTRAP_SSL))
                .map(Boolean::parseBoolean).orElse(false);
        Integer websocketPort = 0;
        if (isWebsocket) {
            websocketPort = Optional.ofNullable(function.apply(BootstrapKey.BOOTSTRAP_WEB_SOCKET_PORT))
                    .map(Integer::parseInt).orElse(DEFAULT_WEBSOCKET_MQTT_PORT);
        }
        String username = Optional.ofNullable(function.apply(BootstrapKey.BOOTSTRAP_USERNAME))
                .map(String::valueOf).orElse(DEFAULT_AUTH_USERNAME_PASSWORD);
        String password = Optional.ofNullable(function.apply(BootstrapKey.BOOTSTRAP_PASSWORD))
                .map(String::valueOf).orElse(DEFAULT_AUTH_USERNAME_PASSWORD);
        Bootstrap.BootstrapBuilder builder = Bootstrap.builder();
        builder.port(port)
                .reactivePasswordAuth(((userName, passwordInBytes) -> userName.equals(username) && password.equals(new String(passwordInBytes))))
                .bossThreadSize(bossThreadSize)
                .wiretap(wiretap)
                .ssl(ssl)
                .workThreadSize(workThreadSize)
                .lowWaterMark(lowWaterMark)
                .highWaterMark(highWaterMark);
        if (isWebsocket) {
            builder.isWebsocket(true)
                    .websocketPort(websocketPort);
        }
        builder.build().startAwait();
    }


}
