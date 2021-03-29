package com.github.smqtt.core;

import com.github.smqtt.common.config.Configuration;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.mqtt.MqttMessage;
import lombok.Data;
import reactor.netty.resources.LoopResources;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author luxurong
 * @date 2021/3/29 20:29
 * @description
 */
@Data
public class ReceiveContext {

    private final Configuration configuration;

    private final LoopResources loopResources;

    private final Function<ByteBuf, MqttMessage> messageDecoder;

    private final Consumer<MqttMessage> messageConsumer;

    private LoopResources loopResource1s;

    public ReceiveContext buildContext() {
        this.loopResource1s = LoopResources.create("smqtt-cluster-io", configuration.bossThreadSize(), configuration.workThreadSize(), true);

    }


}
