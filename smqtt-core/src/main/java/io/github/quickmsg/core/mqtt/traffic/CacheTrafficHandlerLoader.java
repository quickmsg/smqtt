package io.github.quickmsg.core.mqtt.traffic;

import io.github.quickmsg.common.channel.traffic.TrafficHandlerLoader;
import io.netty.handler.traffic.AbstractTrafficShapingHandler;

/**
 * @author luxurong
 */
public class CacheTrafficHandlerLoader implements TrafficHandlerLoader {


    private final AbstractTrafficShapingHandler trafficShapingHandler;

    public CacheTrafficHandlerLoader(AbstractTrafficShapingHandler trafficShapingHandler) {
        this.trafficShapingHandler = trafficShapingHandler;
    }

    @Override
    public AbstractTrafficShapingHandler get() {
        return this.trafficShapingHandler;
    }
}
