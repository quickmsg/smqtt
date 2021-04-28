package io.github.quickmsg.cluster.scalescube;

import io.github.quickmsg.common.cluster.ClusterNode;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author luxurong
 */
@Data
@AllArgsConstructor
public class ScubeClusterNode implements ClusterNode {

    private Object member;
}
