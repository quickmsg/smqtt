package com.github.quickmsg;

import com.github.quickmsg.common.config.SslContext;
import com.github.quickmsg.core.Bootstrap;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class BootstrapTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void TestBootstrap() throws InterruptedException {

//        Bootstrap.builder()
//                .port(8555)
//                .websocketPort(8999)
//                .options(channelOptionMap -> {})
//                .ssl(false)
//                .sslContext(new SslContext("crt","key"))
//                .isWebsocket(true)
//                .wiretap(false)
//                .httpOptions(Bootstrap.HttpOptions.builder().ssl(false).httpPort(62212).accessLog(true).build())
//                .build()
//                .startAwait();
        // 启动服务
        Bootstrap bootstrap = Bootstrap.builder()
                .port(8555)
                .websocketPort(8999)
                .options(channelOptionMap -> {})
                .ssl(false)
                .sslContext(new SslContext("crt","key"))
                .isWebsocket(true)
                .wiretap(true)
                .httpOptions(Bootstrap.HttpOptions.builder().ssl(false).httpPort(62212).accessLog(true).build())
                .build()
                .start().block();
        assert bootstrap != null;
        // 关闭服务
//        bootstrap.shutdown();
        Thread.sleep(1000000);

    }
}
