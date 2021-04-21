package com.github.smqtt;

import org.junit.Test;
import reactor.core.publisher.Sinks;

/**
 * Unit test for simple App.
 */
public class BootstrapTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void TestBootstrap() {

        Bootstrap.builder()
                .port(8555)
                .websocketPort(8999)
//                .options(channelOptionMap -> channelOptionMap.put())
                .ssl(false)
                .isWebsocket(true)
                .wiretap(false)
                .httpOptions(Bootstrap.HttpOptions.builder().accessLog(true).wiretap(true).build())
                .build()
                .startAwait();

    }
}
