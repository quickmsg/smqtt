package io.github.quickmsg.cluster.scalescube;

import io.github.quickmsg.common.cluster.ClusterConfig;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author luxurong
 */
@Data
public class ScubeClusterConfig implements ClusterConfig {

    private String address;

    private Integer port;

    private String nodeName;

    private List<String> clusterUrl;

    private Map<String, Object> options;


}
