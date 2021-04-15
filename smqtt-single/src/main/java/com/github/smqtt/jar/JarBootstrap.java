package com.github.smqtt.jar;

import com.github.smqtt.Bootstrap;
import com.github.smqtt.BootstrapKey;
import io.netty.channel.WriteBufferWaterMark;

import java.util.Optional;

/**
 * @author luxurong
 * @date 2021/4/14 20:39
 * @description
 */
public class JarBootstrap {


    public static void main(String[] args) {
        Integer port = Optional.ofNullable(System.getProperty(BootstrapKey.BOOTSTRAP_PORT))
                .map(Integer::parseInt).orElse(1883);
        Integer lowWaterMark = Optional.ofNullable(System.getProperty(BootstrapKey.BOOTSTRAP_LOW_WATERMARK))
                .map(Integer::parseInt).orElse(WriteBufferWaterMark.DEFAULT.low());
        Integer highWaterMark = Optional.ofNullable(System.getProperty(BootstrapKey.BOOTSTRAP_HIGH_WATERMARK))
                .map(Integer::parseInt).orElse(WriteBufferWaterMark.DEFAULT.high());
        Boolean wiretap = Optional.ofNullable(System.getProperty(BootstrapKey.BOOTSTRAP_WIRETAP))
                .map(Boolean::parseBoolean).orElse(false);
        Integer bossThreadSize = Optional.ofNullable(System.getProperty(BootstrapKey.BOOTSTRAP_BOSS_THREAD_SIZE))
                .map(Integer::parseInt).orElse(Runtime.getRuntime().availableProcessors());
        Integer workThreadSize = Optional.ofNullable(System.getProperty(BootstrapKey.BOOTSTRAP_WORK_THREAD_SIZE))
                .map(Integer::parseInt).orElse(Runtime.getRuntime().availableProcessors());

        Boolean isWebsocket = Optional.ofNullable(System.getProperty(BootstrapKey.BOOTSTRAP_WEB_SOCKET_ENABLE))
                .map(Boolean::parseBoolean).orElse(false);
        Integer websocketPort = 0;
        if (isWebsocket) {
            websocketPort = Optional.ofNullable(System.getProperty(BootstrapKey.BOOTSTRAP_WEB_SOCKET_PORT))
                    .map(Integer::parseInt).orElse(8999);
        }
        String username = Optional.ofNullable(System.getProperty(BootstrapKey.BOOTSTRAP_USERNAME))
                .map(String::valueOf).orElse("smqtt");
        String password = Optional.ofNullable(System.getProperty(BootstrapKey.BOOTSTRAP_PASSWORD))
                .map(String::valueOf).orElse("smqtt");
        Bootstrap.BootstrapBuilder builder = Bootstrap.builder();
        builder.port(port)
                .reactivePasswordAuth(((userName, passwordInBytes) -> userName.equals(username) && password.equals(new String(passwordInBytes))))
                .bossThreadSize(bossThreadSize)
                .wiretap(wiretap)
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
