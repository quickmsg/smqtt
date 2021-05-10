package io.github.quickmsg.common.cluster;

import lombok.*;

/**
 * @author luxurong
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ClusterMessage {

    private String topic;

    private int qos;

    private boolean retain;

    private byte[] message;
}
