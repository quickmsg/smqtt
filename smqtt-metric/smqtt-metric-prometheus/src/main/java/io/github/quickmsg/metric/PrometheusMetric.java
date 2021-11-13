package io.github.quickmsg.metric;

import io.github.quickmsg.common.metric.CounterEnum;
import io.github.quickmsg.common.metric.MetircConstant;
import io.github.quickmsg.common.metric.Metric;
import io.github.quickmsg.common.metric.MetricCounter;
import io.github.quickmsg.metric.counter.ConnectCounter;
import io.github.quickmsg.metric.counter.TopicCounter;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.exporter.common.TextFormat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * prometheus度量
 *
 * @author easy
 * @date 2021/11/12
 */
public class PrometheusMetric implements Metric {

    public static final PrometheusMeterRegistry PROMETHEUS_METER_REGISTRY_INSTANCE = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

    public static final Map<CounterEnum, MetricCounter> map = new HashMap<>();

    @Override
    public void init() {
        Metrics.globalRegistry.config().commonTags(Arrays.asList(Tag.of(MetircConstant.commonTagName, MetircConstant.commonTagValue)));
        Metrics.globalRegistry.add(PROMETHEUS_METER_REGISTRY_INSTANCE);
        new ClassLoaderMetrics().bindTo(Metrics.globalRegistry);
        new JvmMemoryMetrics().bindTo(Metrics.globalRegistry);
        new JvmGcMetrics().bindTo(Metrics.globalRegistry);
        new ProcessorMetrics().bindTo(Metrics.globalRegistry);
        new JvmThreadMetrics().bindTo(Metrics.globalRegistry);

        map.put(CounterEnum.CONNECT_COUNTER, ConnectCounter.CONNECT_SIZE_COUNTER_INSTANCE);
        map.put(CounterEnum.TOPIC_COUNTER, TopicCounter.TOPIC_COUNTER_INSTANCE);
    }

    public MetricCounter getMetricCounter(CounterEnum counterEnum) {
        return map.get(counterEnum);
    }

    public String scrape() {
        return PROMETHEUS_METER_REGISTRY_INSTANCE.scrape(TextFormat.CONTENT_TYPE_OPENMETRICS_100);
    }


}
