package io.github.quickmsg.common.cluster;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author luxurong
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClusterMessage {

    private String topic;

    private int qos;

    private boolean retain;

    private byte[] message;


}
