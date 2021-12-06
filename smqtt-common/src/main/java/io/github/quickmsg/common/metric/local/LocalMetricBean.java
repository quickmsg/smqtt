package io.github.quickmsg.common.metric.local;

import io.github.quickmsg.common.metric.MetricBean;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.logging.LoggingMeterRegistry;

/**
 * @author luxurong
 */
public class LocalMetricBean implements MetricBean {

    private final MeterRegistry meterRegistry;

    public LocalMetricBean() {
        this.meterRegistry = new LoggingMeterRegistry();
    }

    @Override
    public MetricBean Close() {
        meterRegistry.close();
        return this;
    }

    @Override
    public MeterRegistry getMeterRegistry() {
        return this.meterRegistry;
    }
}
