package com.github.smqtt;

import org.junit.Test;

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
                .build()
                .startAwait();

    }
}
