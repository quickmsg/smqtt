package io.github.quickmsg.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author luxurong
 */
@Data
public class StarterConfig {


    @JsonProperty("smqtt")
    private SmqttConfig smqttConfig;

    @Data
    public static class SmqttConfig {

        @JsonProperty("tcp")
        private TcpConfig tcpConfig;

        @JsonProperty("http")
        private HttpConfig httpConfig;

        @JsonProperty("ws")
        private WebsocketConfig websocketConfig;


        @JsonProperty("cluster")
        private ClusterConfig clusterConfig;

    }

    @Data
    private static class TcpConfig {

        private String port;
    }

    private static class HttpConfig {
    }

    private static class WebsocketConfig {
    }

    private static class ClusterConfig {
    }


}
