package com.github.smqtt.common.context;

import com.github.smqtt.common.config.Configuration;
import com.github.smqtt.common.transport.Transport;
import lombok.Getter;
import lombok.Setter;
import reactor.netty.resources.LoopResources;

/**
 * @author luxurong
 * @date 2021/3/29 20:29
 * @description
 */
@Getter
@Setter
public abstract class ReceiveContext<T extends Configuration> {

    private T configuration;

    private LoopResources loopResources;

    private Transport<T> transport;

    public ReceiveContext(T configuration, Transport<T> transport) {
        this.configuration = configuration;
        this.transport = transport;
        this.loopResources = LoopResources.create("smqtt-cluster-io", configuration.getBossThreadSize(), configuration.getWorkThreadSize(), true);
    }


}
