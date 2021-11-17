package io.github.quickmsg.metric;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.quickmsg.common.config.BootstrapConfig;
import io.github.quickmsg.common.metric.*;
import io.github.quickmsg.common.utils.FormatUtils;
import io.github.quickmsg.metric.counter.ConnectCounter;
import io.github.quickmsg.metric.counter.ReadWriteSideWindowCounter;
import io.github.quickmsg.metric.counter.TopicCounter;
import io.micrometer.core.instrument.*;
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.influx.InfluxConfig;
import io.micrometer.influx.InfluxMeterRegistry;

import java.text.DecimalFormat;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

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

    private ReadWriteSideWindowCounter readHourSize = ReadWriteSideWindowCounter.getInstance(1, TimeUnit.HOURS, "READ-HOUR-SIZE", this);
    private ReadWriteSideWindowCounter writeHourSize = ReadWriteSideWindowCounter.getInstance(1, TimeUnit.HOURS, "WRITE-HOUR-SIZE", this);

    @Override
    public boolean init(BootstrapConfig.MeterConfig meterConfig) {
        config = new InfluxConfig() {
            @Override
            public Duration step() {
                return Duration.ofSeconds(meterConfig.getInfluxdb1().getStep());
            }

            @Override
            public String db() {
                return meterConfig.getInfluxdb1().getDb();
            }

            @Override
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

        return true;
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
    public List<Double> scrapeByMeterId(Meter.Id meterId, Statistic statistic) {
        List<Meter> meterList = INFLUX_METER_REGISTRY_INSTANCE.getMeters();
        List<Double> valueList = new ArrayList<>();
        meterList.stream()
                .filter(meter -> {
                    if (meterId.getTags() == null || meterId.getTags().size() <= 0) {
                        return meter.getId().getName().equals(meterId.getName());
                    } else {
                        return meter.getId().equals(meterId);
                    }
                }).forEach(meter -> {
            meter.measure().forEach(measurement -> {
                if (measurement.getStatistic() == statistic) {
                    valueList.add(measurement.getValue());
                }
            });
        });

        return valueList;
    }

    @Override
    public ObjectNode scrapeCounter() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode window = objectMapper.createObjectNode();

        double readSize = formatValue(scrapeByMeterId(new Meter.Id(MetircConstant.REACTOR_NETTY_TCP_SERVER_DATA_RECEIVED, Tags.empty(), null, null, null), Statistic.TOTAL));
        double writeSize = formatValue(scrapeByMeterId(new Meter.Id(MetircConstant.REACTOR_NETTY_TCP_SERVER_DATA_SENT, Tags.empty(), null, null, null), Statistic.TOTAL));

        window.put("connect_size", formatValue(scrapeByMeterId(new Meter.Id(MetircConstant.CONNECT_COUNTER_NAME, Tags.empty(), null, null, null), Statistic.VALUE)));
        window.put("read_size", FormatUtils.formatByte(readSize));
        window.put("read_hour_size", FormatUtils.formatByte(readSize - readHourSize.getLastReadSize()));
        window.put("write_size", FormatUtils.formatByte(writeSize));
        window.put("write_hour_size", FormatUtils.formatByte(writeSize - writeHourSize.getLastWriteSize()));
        return window;
    }

    @Override
    public ObjectNode scrapeCpu() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode cpuInfo = objectMapper.createObjectNode();

        Tags tags = Tags.empty().and(MetircConstant.COMMON_TAG_NAME, MetircConstant.COMMON_TAG_VALUE);
        //cpu核数
        cpuInfo.put("cpuNum", formatValue(scrapeByMeterId(new Meter.Id(MetircConstant.SYSTEM_CPU_COUNT, tags, null, null, null), Statistic.VALUE)));
        //cpu系统使用率
        cpuInfo.put("cSys", new DecimalFormat("#.##%").format(formatValue(scrapeByMeterId(new Meter.Id(MetircConstant.SYSTEM_CPU_USAGE, tags, null, null, null), Statistic.VALUE))));
        //cpu用户使用率
        cpuInfo.put("user", new DecimalFormat("#.##%").format(formatValue(scrapeByMeterId(new Meter.Id(MetircConstant.PROCESS_CPU_USAGE, tags, null, null, null), Statistic.VALUE))));
        //cpu当前等待率
        cpuInfo.put("iowait", "N/A");
        //cpu当前使用率
        cpuInfo.put("idle", "N/A");
        return cpuInfo;
    }
}
