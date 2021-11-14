package io.github.quickmsg.metric;

import io.github.quickmsg.common.config.BootstrapConfig;
import io.github.quickmsg.common.metric.*;
import io.github.quickmsg.metric.counter.ConnectCounter;
import io.github.quickmsg.metric.counter.TopicCounter;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.influx.InfluxConfig;
import io.micrometer.influx.InfluxMeterRegistry;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * influx度量
 *
 * @author easy
 * @date 2021/11/13
 */
public class Influx1Metric implements Metric {

    public static MeterRegistry INFLUX_METER_REGISTRY_INSTANCE = null;

    public static final Map<CounterEnum, MetricCounter> map = new HashMap<>();

    @Override
    public DatabaseEnum getDatabaseType() {
        return DatabaseEnum.INFLUXDB1;
    }

    @Override
    public void init(BootstrapConfig.MeterConfig meterConfig) {
        InfluxConfig config = new InfluxConfig() {
            @Override
            public Duration step() {
                return Duration.ofSeconds(meterConfig.getInfluxdb1().getStep());
            }

            @Override
            public String db() {
                return meterConfig.getInfluxdb1().getDb();
            }

            public String uri() {
                return meterConfig.getInfluxdb1().getUri();
            }

            @Override
            public String get(String k) {
                return null;
            }
        };

        INFLUX_METER_REGISTRY_INSTANCE = new InfluxMeterRegistry(config, Clock.SYSTEM);

        Metrics.globalRegistry.config().commonTags(Arrays.asList(Tag.of(MetircConstant.commonTagName, MetircConstant.commonTagValue)));
        Metrics.globalRegistry.add(INFLUX_METER_REGISTRY_INSTANCE);
        new ClassLoaderMetrics().bindTo(Metrics.globalRegistry);
        new JvmMemoryMetrics().bindTo(Metrics.globalRegistry);
        new JvmGcMetrics().bindTo(Metrics.globalRegistry);
        new ProcessorMetrics().bindTo(Metrics.globalRegistry);
        new JvmThreadMetrics().bindTo(Metrics.globalRegistry);

        map.put(CounterEnum.CONNETC_COUNTER, ConnectCounter.CONNECT_SIZE_COUNTER_INSTANCE);
        map.put(CounterEnum.TOPIC_COUNTER, TopicCounter.TOPIC_COUNTER_INSTANCE);
    }

    public MetricCounter getMetricCounter(CounterEnum counterEnum) {
        return map.get(counterEnum);
    }

    public String scrape() {
        return null;
    }




}
