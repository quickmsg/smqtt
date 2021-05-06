package io.github.quickmsg.common.cluster;

import lombok.Builder;
import lombok.Data;

/**
 * @author luxurong
 */
@Data
@Builder
public class ClusterNode {

    /**
     * 别名
     */
    private String alias;

    /**
     * host
     */
    private String host;


    /**
     * 端口
     */
    private Integer port;


    /**
     * 命名空间
     */
    private String namespace;

}
