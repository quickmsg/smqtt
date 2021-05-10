import io.github.quickmsg.common.cluster.ClusterConfig;
import io.github.quickmsg.core.Bootstrap;

/**
 * @author luxurong
 * @date 2021/5/6 19:25
 * @description
 */
public class ClusterNode3 {

    @org.junit.Test
    public void startServer() throws InterruptedException {
        Bootstrap bootstrap = Bootstrap.builder()
                .port(8551)
                .options(channelOptionMap -> {})//netty options设置
                .childOptions(channelOptionMap ->{}) //netty childOptions设置
                .highWaterMark(1000000)
                .reactivePasswordAuth((U,P)->true)
                .lowWaterMark(1000)
                .ssl(false)
                .wiretap(true)
                .clusterConfig(
                        ClusterConfig.builder()
                        .clustered(true)
                                .port(7771)
                                .nodeName("node-4")
                                .clusterUrl("127.0.0.1:7772,127.0.0.1:7773")
                                .build())
                .build()
                .start().block();
        assert bootstrap != null;
        // 关闭服
//        bootstrap.shutdown();
        Thread.sleep(1000000);
    }
}
