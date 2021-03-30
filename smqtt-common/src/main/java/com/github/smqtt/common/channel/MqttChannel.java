package com.github.smqtt.common.channel;

import com.github.smqtt.common.enums.ChannelStatus;
import io.netty.channel.Channel;
import lombok.Data;

/**
 * @author luxurong
 * @date 2021/3/30 13:43
 * @description
 */
@Data
public class MqttChannel {

    private Channel channel;

    private String deviceId;

    private ChannelStatus status;

    private long activeTime;


}
