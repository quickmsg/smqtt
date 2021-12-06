package io.github.quickmsg.common.metric.local;

import io.github.quickmsg.common.metric.MetricBean;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.logging.LoggingMeterRegistry;

/**
 * @author luxurong
 */
public class LocalMetricBean implements MetricBean {


    public LocalMetricBean() {
    }

    @Override
    public MetricBean Close() {
        return this;
    }

    @Override
    public MeterRegistry getMeterRegistry() {
        return null;
    }
}
