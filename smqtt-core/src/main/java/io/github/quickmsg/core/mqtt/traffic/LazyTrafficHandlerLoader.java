package io.github.quickmsg.core.mqtt.traffic;

import io.github.quickmsg.common.channel.traffic.TrafficHandlerLoader;
import io.netty.handler.traffic.AbstractTrafficShapingHandler;

import java.util.function.Supplier;

/**
 * @author luxurong
 */
public class LazyTrafficHandlerLoader implements TrafficHandlerLoader {

    private final Supplier<AbstractTrafficShapingHandler> shapingHandlerSupplier;

    public LazyTrafficHandlerLoader(Supplier<AbstractTrafficShapingHandler> shapingHandlerSupplier) {
        this.shapingHandlerSupplier = shapingHandlerSupplier;
    }

    @Override
    public AbstractTrafficShapingHandler get() {
        return this.shapingHandlerSupplier.get();
    }
}
