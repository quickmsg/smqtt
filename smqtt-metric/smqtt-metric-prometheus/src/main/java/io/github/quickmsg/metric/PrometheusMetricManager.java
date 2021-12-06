package io.github.quickmsg.metric;

import io.github.quickmsg.common.config.BootstrapConfig;
import io.github.quickmsg.common.metric.MetricBean;
import io.github.quickmsg.common.metric.MetricManager;
import io.github.quickmsg.common.metric.MetricRegistry;

/**
 * @author luxurong
 */
public class PrometheusMetricManager implements MetricManager {

    private final BootstrapConfig.MeterConfig config;

    private final MetricBean metricBean;

    private final MetricRegistry metricRegistry;

    public PrometheusMetricManager(BootstrapConfig.MeterConfig config) {
        this.config = config;
        this.metricBean = new PrometheusMetricBean();
        this.metricRegistry = new PrometheusMetricRegistry(createMetricRegistry(metricBean));
    }


    @Override
    public MetricRegistry getMetricRegistry() {
        return this.metricRegistry;
    }

    @Override
    public MetricBean getMetricBean() {
        return this.metricBean;
    }

    @Override
    public BootstrapConfig.MeterConfig getMeterConfig() {
        return this.config;
    }
}
