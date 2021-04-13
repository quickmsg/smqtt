package com.github.smqtt;

import com.github.smqtt.bootstrap.Bootstrap;
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
                .isWebsocket(true)
                .wiretap(true)
                .build()
                .startAwait();

    }
}
