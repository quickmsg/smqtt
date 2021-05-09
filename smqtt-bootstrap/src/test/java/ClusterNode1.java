import io.github.quickmsg.common.cluster.ClusterConfig;
import io.github.quickmsg.common.config.SslContext;
import io.github.quickmsg.core.Bootstrap;

/**
 * @author luxurong
 * @date 2021/5/6 19:25
 * @description
 */
public class ClusterNode1 {

    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrap = Bootstrap.builder()
                .port(8555)
                .websocketPort(8999)
                .options(channelOptionMap -> {})//netty options设置
                .childOptions(channelOptionMap ->{}) //netty childOptions设置
                .highWaterMark(1000000)
                .reactivePasswordAuth((U,P)->true)
                .lowWaterMark(1000)
                .ssl(false)
                .sslContext(new SslContext("crt","key"))
                .isWebsocket(true)
                .wiretap(true)
                .httpOptions(Bootstrap.HttpOptions.builder().ssl(false).httpPort(62212).accessLog(true).build())
                .clusterConfig(
                        ClusterConfig.builder()
                                .clustered(true)
                                .port(7773)
                                .nodeName("node-2")
                                .clusterUrl("127.0.0.1:7771,127.0.0.1:7772")
                                .build())
                .build()
                .start().block();
        assert bootstrap != null;
        // 关闭服
//        bootstrap.shutdown();
        Thread.sleep(1000000);
    }

}
