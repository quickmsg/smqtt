package com.github.quickmsg;

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
                .httpOptions(Bootstrap.HttpOptions.builder().httpPort(62212).accessLog(true).build())
                .build()
                .startAwait();

    }
}
