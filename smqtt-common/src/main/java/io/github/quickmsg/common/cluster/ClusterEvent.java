package io.github.quickmsg.common.cluster;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author luxurong
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClusterEvent<T, N extends ClusterNode> {


    private T event;

    private N n;

}
