package com.github.smqtt.core.mqtt;

import com.github.smqtt.common.channel.ChannelRegistry;
import com.github.smqtt.common.config.Configuration;
import lombok.Getter;

/**
 * @author luxurong
 * @date 2021/3/30 13:26
 * @description
 */
@Getter
public class MqttConfiguration implements Configuration {


    private Class<ChannelRegistry> channelRegistry;


    @Override
    public int getBossThreadSize() {
        return 0;
    }

    @Override
    public int getWorkThreadSize() {
        return 0;
    }

    @Override
    public int getPort() {
        return 0;
    }
}
