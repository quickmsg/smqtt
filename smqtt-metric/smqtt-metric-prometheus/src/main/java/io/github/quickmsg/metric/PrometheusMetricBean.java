package io.github.quickmsg.metric;

import io.github.quickmsg.common.config.BootstrapConfig;
import io.github.quickmsg.common.metric.MetricBean;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;

/**
 * @author luxurong
 */
public class PrometheusMetricBean implements MetricBean {

    public final PrometheusMeterRegistry prometheusMeterRegistry;

    public PrometheusMetricBean() {
        prometheusMeterRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
    }

    @Override
    public MetricBean Close() {
        prometheusMeterRegistry.close();
        return this;
    }

    @Override
    public MeterRegistry getMeterRegistry() {
        return this.prometheusMeterRegistry;
    }
}
