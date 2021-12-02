package io.github.quickmsg.metric;

import io.github.quickmsg.common.metric.MetricBean;
import io.github.quickmsg.common.metric.MetricConstant;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;

import java.util.Collections;

/**
 * @author luxurong
 */
public class PrometheusMetricBean implements MetricBean {

    public final PrometheusMeterRegistry prometheusMeterRegistry;

    public PrometheusMetricBean() {
        prometheusMeterRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        Metrics.globalRegistry.config().commonTags(getTags());
        Metrics.globalRegistry.add(prometheusMeterRegistry);
        new ClassLoaderMetrics().bindTo(Metrics.globalRegistry);
        new JvmMemoryMetrics().bindTo(Metrics.globalRegistry);
        new JvmGcMetrics().bindTo(Metrics.globalRegistry);
        new ProcessorMetrics().bindTo(Metrics.globalRegistry);
        new JvmThreadMetrics().bindTo(Metrics.globalRegistry);
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
