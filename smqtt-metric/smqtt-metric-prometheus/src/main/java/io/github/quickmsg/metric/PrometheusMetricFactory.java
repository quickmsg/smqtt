package io.github.quickmsg.metric;

import io.github.quickmsg.common.config.BootstrapConfig;
import io.github.quickmsg.common.metric.MetricFactory;
import io.github.quickmsg.common.metric.MetricManager;

/**
 * @author luxurong
 */
public class PrometheusMetricFactory implements MetricFactory {

    private final MetricManager metricManager;

    public PrometheusMetricFactory(BootstrapConfig.MeterConfig config) {
        this.metricManager = new PrometheusMetricManager(config);
    }

    @Override
    public MetricManager getMetricManager() {
        return this.metricManager;
    }
}
