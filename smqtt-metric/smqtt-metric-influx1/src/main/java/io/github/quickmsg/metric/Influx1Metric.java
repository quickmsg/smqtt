package io.github.quickmsg.metric;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.quickmsg.common.config.BootstrapConfig;
import io.github.quickmsg.common.metric.*;
import io.github.quickmsg.common.utils.FormatUtils;
import io.github.quickmsg.metric.counter.ConnectCounter;
import io.github.quickmsg.metric.counter.TopicCounter;
import io.micrometer.core.instrument.*;
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.core.instrument.util.MeterPartition;
import io.micrometer.influx.InfluxConfig;
import io.micrometer.influx.InfluxMeterRegistry;

import java.time.Duration;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

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

    private static InfluxConfig config;

    @Override
    public void init(BootstrapConfig.MeterConfig meterConfig) {
        config = new InfluxConfig() {
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

        Metrics.globalRegistry.config().commonTags(Arrays.asList(Tag.of(MetircConstant.COMMON_TAG_NAME, MetircConstant.COMMON_TAG_VALUE)));
        Metrics.globalRegistry.add(INFLUX_METER_REGISTRY_INSTANCE);
        new ClassLoaderMetrics().bindTo(Metrics.globalRegistry);
        new JvmMemoryMetrics().bindTo(Metrics.globalRegistry);
        new JvmGcMetrics().bindTo(Metrics.globalRegistry);
        new ProcessorMetrics().bindTo(Metrics.globalRegistry);
        new JvmThreadMetrics().bindTo(Metrics.globalRegistry);

        map.put(CounterEnum.CONNECT_COUNTER, ConnectCounter.CONNECT_SIZE_COUNTER_INSTANCE);
        map.put(CounterEnum.TOPIC_COUNTER, TopicCounter.TOPIC_COUNTER_INSTANCE);
    }

    @Override
    public MetricCounter getMetricCounter(CounterEnum counterEnum) {
        return map.get(counterEnum);
    }

    @Override
    public String scrape() {
        return null;
    }

    @Override
    public List<Double> scrapeByMeterId(Meter.Id meterId) {
        List<Meter> list = INFLUX_METER_REGISTRY_INSTANCE.getMeters();
        Optional<List<Double>> first = list.stream()
                .filter(meter -> meter.getId().equals(meterId))
                .map(meter -> {
                    List<Double> valueList = new ArrayList<>();
                    meter.measure().forEach(measurement -> valueList.add(measurement.getValue()));
                    return valueList;
                }).findFirst();
        return first.isPresent() ? first.get() : null;
    }

    @Override
    public ObjectNode scrapeCounter() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode window = objectMapper.createObjectNode();

        window.put("connect_size", formatValue(scrapeByMeterId(new Meter.Id(MetircConstant.CONNECT_COUNTER_NAME, Tags.empty(), null, null, Meter.Type.GAUGE))));
        window.put("read_size", 0);
        window.put("read_hour_size", 0);
        window.put("write_size", 0);
        window.put("write_hour_size", 0);
        return window;
    }

    public ObjectNode scrapeCpu() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode window = objectMapper.createObjectNode();
        return window;
    }
}
