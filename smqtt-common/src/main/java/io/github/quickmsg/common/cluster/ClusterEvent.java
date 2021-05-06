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
public class ClusterEvent<T> {


    private T event;

    private ClusterNode n;

}
