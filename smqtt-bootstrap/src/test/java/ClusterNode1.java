import ch.qos.logback.classic.Level;
import io.github.quickmsg.common.config.BootstrapConfig;
import io.github.quickmsg.core.Bootstrap;

/**
 * @author luxurong
 */
public class ClusterNode1 {

    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrap = Bootstrap.builder()
                .rootLevel(Level.ERROR)
                .websocketConfig(
                        BootstrapConfig.WebsocketConfig
                                .builder()
                                .enable(false)
                                .path("/mqtt")
                                .port(8880)
                                .build()
                )
                .tcpConfig(
                        BootstrapConfig
                                .TcpConfig
                                .builder()
                                .port(8888)
                                .username("smqtt")
                                .password("smqtt")
                                .build())
                .httpConfig(
                        BootstrapConfig
                                .HttpConfig
                                .builder()
                                .enable(true)
                                .accessLog(true)
                                .admin(BootstrapConfig.HttpAdmin.builder().enable(true).username("smqtt").password("smqtt").build())
                                .build())
                .clusterConfig(
                        BootstrapConfig.
                                ClusterConfig
                                .builder()
                                .enable(false)
                                .namespace("smqtt")
                                .node("node-1")
                                .port(7773)
                                .url("127.0.0.1:7771,127.0.0.1:7772").
                                build())
                .build()
                .start().block();
        assert bootstrap != null;
        Thread.sleep(10000000);
    }

}
