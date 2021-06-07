package io.github.quickmsg.common.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author luxurong
 */
@Getter
@AllArgsConstructor
public class RetainMessage {

    private int qos;

    private String topic;

    private byte[] body;

}
