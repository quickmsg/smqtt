import io.github.quickmsg.core.Bootstrap;

/**
 * @author luxurong
 * @date 2021/5/6 19:25
 * @description
 */
public class Test {

    @org.junit.Test
    public void startServer() throws InterruptedException {
        Bootstrap bootstrap = Bootstrap.builder()
                .port(8555)
                .websocketPort(8999)
                .options(channelOptionMap -> {
                })
                .highWaterMark(1000000)
                .lowWaterMark(1000)
                .ssl(false)
//                .sslContext(new SslContextontext("crt","key"))
                .isWebsocket(true)
                .wiretap(true)
                .httpOptions(Bootstrap.HttpOptions.builder().ssl(false).httpPort(62212).accessLog(true).build())
                .build()
                .start().block();
        assert bootstrap != null;
        // 关闭服
//        bootstrap.shutdown();
        Thread.sleep(1000000);
    }
}
