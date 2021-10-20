package io.github.quickmsg.common.message.system;

import io.github.quickmsg.common.enums.ChannelStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author luxurong
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChannelStatusMessage {

    /**
     * clint id
     */
    private String clientIdentifier;

    /**
     * timestamp
     */
    private long timestamp;

    /**
     * username
     */
    private String username;

    /**
     * channelStatus
     *
     * @see ChannelStatus
     */
    private ChannelStatus channelStatus;

}
