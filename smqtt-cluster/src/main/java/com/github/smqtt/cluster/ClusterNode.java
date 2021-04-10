package com.github.smqtt.cluster;

import lombok.Data;

/**
 * @author luxurong
 * @date 2021/4/9 17:59
 * @description
 */
@Data
public class ClusterNode {

    private String nodeName;

    private String  address;
}
