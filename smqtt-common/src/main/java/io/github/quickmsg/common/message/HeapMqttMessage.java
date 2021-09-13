package io.github.quickmsg.common.message;

import lombok.*;

/**
 * @author luxurong
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class HeapMqttMessage {

    private String clientIdentifier;

    private String topic;

    private int qos;

    private boolean retain;

    private byte[] message;


}
