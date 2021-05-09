import io.github.quickmsg.common.cluster.ClusterConfig;
import io.github.quickmsg.common.config.SslContext;
import io.github.quickmsg.core.Bootstrap;

/**
 * @author luxurong
 * @date 2021/5/6 19:25
 * @description
 */
public class ClusterNode2 {

    @org.junit.Test
    public void startServer() throws InterruptedException {
        Bootstrap bootstrap = Bootstrap.builder()
                .port(8556)
                .options(channelOptionMap -> {})//netty options设置
                .childOptions(channelOptionMap ->{}) //netty childOptions设置
                .highWaterMark(1000000)
                .lowWaterMark(1000)
                .reactivePasswordAuth((U,P)->true)
                .ssl(false)
                .wiretap(true)
                .clusterConfig(
                        ClusterConfig.builder()
                        .clustered(true)
                                .port(7772)
                                .nodeName("node-3")
                                .clusterUrl("127.0.0.1:7771,127.0.0.1:7773")
                                .build())
                .build()
                .start().block();
        assert bootstrap != null;
        // 关闭服
//        bootstrap.shutdown();
        Thread.sleep(1000000);
    }
}
