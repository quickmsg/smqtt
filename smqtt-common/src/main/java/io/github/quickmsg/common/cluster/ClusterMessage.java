package io.github.quickmsg.common.cluster;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author luxurong
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ClusterMessage {

    private String topic;

    private int qos;

    private boolean retain;

    private byte[] message;


}
