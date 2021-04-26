package com.github.quickmsg.cluster.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by  lxr.
 * User: luxurong
 * Date: 2021/4/10
 *
 * @author luxurong
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClusterEvent<T, N extends ClusterNode> {


    private T event;

    private N n;

}
