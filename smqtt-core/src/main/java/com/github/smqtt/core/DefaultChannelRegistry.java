package com.github.smqtt.core;

import com.github.smqtt.common.channel.ChannelRegistry;
import com.github.smqtt.common.channel.MqttChannel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.DefaultEventExecutor;

/**
 * @author luxurong
 * @date 2021/3/30 19:20
 * @description
 */
public class DefaultChannelRegistry implements ChannelRegistry {



    private ChannelGroup group = new DefaultChannelGroup(new DefaultEventExecutor());


    @Override
    public void close(MqttChannel mqttChannel) {

    }

    @Override
    public void registry(String clientIdentifier, MqttChannel mqttChannel) {

    }

    @Override
    public boolean exists(String clientIdentifier) {
        return false;
    }
}
