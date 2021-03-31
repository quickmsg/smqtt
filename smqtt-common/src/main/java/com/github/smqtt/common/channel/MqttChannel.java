package com.github.smqtt.common.channel;

import com.github.smqtt.common.enums.ChannelStatus;
import lombok.Builder;
import lombok.Data;
import reactor.netty.Connection;

/**
 * @author luxurong
 * @date 2021/3/30 13:43
 * @description
 */
@Data
@Builder
public class MqttChannel {

    private Connection connection;

    private String deviceId;

    private ChannelStatus status;

    private long activeTime;


}
