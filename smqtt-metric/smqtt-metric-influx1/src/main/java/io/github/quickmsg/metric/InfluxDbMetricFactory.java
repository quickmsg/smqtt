package io.github.quickmsg.metric;

import io.github.quickmsg.common.config.BootstrapConfig;
import io.github.quickmsg.metric.api.MetricFactory;
import io.github.quickmsg.metric.api.MetricManager;

/**
 * @author luxurong
 */
public class InfluxDbMetricFactory implements MetricFactory {

    private final MetricManager metricManager;

    public InfluxDbMetricFactory(BootstrapConfig.MeterConfig config) {
        this.metricManager = new InfluxDbMetricManager(config);
    }

    @Override
    public MetricManager getMetricManager() {
        return this.metricManager;
    }
}
