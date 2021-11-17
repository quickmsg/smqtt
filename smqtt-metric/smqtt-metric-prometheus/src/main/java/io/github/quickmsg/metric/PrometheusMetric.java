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
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.exporter.common.TextFormat;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
    public DatabaseEnum getDatabaseType() {
        return DatabaseEnum.PROMETHEUS;
    }

    private ReadWriteSideWindowCounter readHourSize = ReadWriteSideWindowCounter.getInstance(1, TimeUnit.HOURS, "READ-HOUR-SIZE", this);
    private ReadWriteSideWindowCounter writeHourSize = ReadWriteSideWindowCounter.getInstance(1, TimeUnit.HOURS, "WRITE-HOUR-SIZE", this);

    public PrometheusMetric() {

    }

    @Override
    public boolean init(BootstrapConfig.MeterConfig meterConfig) {
        Metrics.globalRegistry.config().commonTags(Arrays.asList(Tag.of(MetircConstant.COMMON_TAG_NAME, MetircConstant.COMMON_TAG_VALUE)));
        Metrics.globalRegistry.add(PROMETHEUS_METER_REGISTRY_INSTANCE);
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
        return PROMETHEUS_METER_REGISTRY_INSTANCE.scrape(TextFormat.CONTENT_TYPE_OPENMETRICS_100);
    }

    @Override
    public List<Double> scrapeByMeterId(Meter.Id meterId, Statistic statistic) {
        List<Meter> meterList = PROMETHEUS_METER_REGISTRY_INSTANCE.getMeters();
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
