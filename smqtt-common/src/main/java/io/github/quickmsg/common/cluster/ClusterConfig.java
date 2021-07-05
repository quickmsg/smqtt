package io.github.quickmsg.common.cluster;

import lombok.Builder;
import lombok.Data;

/**
 * @author luxurong
 */
@Data
@Builder
public class ClusterConfig {

    private Boolean clustered;

    private Integer port;

    private String host;

    private String nodeName;

    private String clusterUrl;

    @Builder.Default
    private String namespace = "smqtt";

    public static ClusterConfig defaultClusterConfig() {
        return ClusterConfig.builder()
                .clustered(false)
                .host("0.0.0.0")
                .port(0)
                .build();
    }
}
