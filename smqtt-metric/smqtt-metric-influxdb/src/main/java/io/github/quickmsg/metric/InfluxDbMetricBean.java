package io.github.quickmsg.metric;

import io.github.quickmsg.common.config.BootstrapConfig;
import io.github.quickmsg.common.metric.MetricBean;
import io.github.quickmsg.common.metric.MetricConstant;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.influx.InfluxConfig;
import io.micrometer.influx.InfluxMeterRegistry;

import java.time.Duration;
import java.util.Objects;

/**
 * @author luxurong
 */
public class InfluxDbMetricBean implements MetricBean, InfluxConfig {


    private final BootstrapConfig.MeterConfig meterConfig;

    public final MeterRegistry meterRegistry;

    public InfluxDbMetricBean(BootstrapConfig.MeterConfig meterConfig) {
        this.meterConfig = meterConfig;
        checkConfig(meterConfig);
        this.meterRegistry = new InfluxMeterRegistry(this, Clock.SYSTEM);
        Metrics.globalRegistry.config().commonTags(getTags());
        Metrics.globalRegistry.add(meterRegistry);
        new ClassLoaderMetrics().bindTo(Metrics.globalRegistry);
        new JvmMemoryMetrics().bindTo(Metrics.globalRegistry);
        new JvmGcMetrics().bindTo(Metrics.globalRegistry);
        new ProcessorMetrics().bindTo(Metrics.globalRegistry);
        new JvmThreadMetrics().bindTo(Metrics.globalRegistry);
    }

    private void checkConfig(BootstrapConfig.MeterConfig meterConfig) {
        Objects.requireNonNull(meterConfig.getInfluxdb());
        Objects.requireNonNull(meterConfig.getInfluxdb().getDb());
        Objects.requireNonNull(meterConfig.getInfluxdb().getUri());
        Objects.requireNonNull(meterConfig.getInfluxdb().getPassword());
        Objects.requireNonNull(meterConfig.getInfluxdb().getUserName());
    }

    @Override
    public Duration step() {
        return Duration.ofSeconds(Math.max(meterConfig.getInfluxdb().getStep(), 10));
    }

    @Override
    public String db() {
        return meterConfig.getInfluxdb().getDb();
    }

    @Override
    public String uri() {
        return meterConfig.getInfluxdb().getUri();
    }

    @Override
    public String get(String k) {
        return null;
    }

    @Override
    public String userName() {
        return meterConfig.getInfluxdb().getUserName();
    }

    @Override
    public String password() {
        return meterConfig.getInfluxdb().getPassword();
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
