package io.github.quickmsg.common.metric;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.quickmsg.common.config.BootstrapConfig;
import io.github.quickmsg.common.utils.FormatUtils;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Statistic;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * 度量
 *
 * @author easy
 * @date 2021/11/12
 */
public interface Metric {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 初始化
     */
    boolean init(BootstrapConfig.MeterConfig meterConfig);

    /**
     * 获取计数器
     *
     * @param counterEnum 类型枚举
     * @return {@link MetricCounter}
     */
    MetricCounter getMetricCounter(CounterEnum counterEnum);

    /**
     * 样本数据手机
     *
     * @return {@link String}
     */
    String scrape();


    /**
     * 获取数据库类型
     *
     * @return {@link DatabaseEnum}
     */
    DatabaseEnum getDatabaseType();


    /**
     * 根据id获取样本数据
     *
     * @param meterId   仪表id
     * @param statistic 类型
     * @return {@link List}<{@link Double}>
     */
    List<Double> scrapeByMeterId(Meter.Id meterId, Statistic statistic);


    /**
     * 格式值
     *
     * @param list 列表
     * @return double
     */
    default double formatValue(List<Double> list) {
        return list.stream().reduce(Double::sum).orElse(0d);
    }

    /**
     * 获取计数器样本值
     *
     * @return {@link ObjectNode}
     */
    ObjectNode scrapeCounter();

    /**
     * 获取jvm样本值
     *
     * @return {@link ObjectNode}
     */
    default ObjectNode scrapeJvm() {
        Properties props = System.getProperties();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jvm = objectMapper.createObjectNode();

        MemoryMXBean mxb = ManagementFactory.getMemoryMXBean();
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
        jvm.put("smqtt", "1.0.8");
        jvm.put("start_time", sdf.format(new Date(runtimeBean.getStartTime())));
        jvm.put("jdk_home", props.getProperty("java.home"));
        jvm.put("jdk_version", props.getProperty("java.version"));
        jvm.put("thread.count", threadBean.getThreadCount());
        jvm.put("heap-max", FormatUtils.formatByte(mxb.getHeapMemoryUsage().getMax()));
        jvm.put("heap-init", FormatUtils.formatByte(mxb.getHeapMemoryUsage().getInit()));
        jvm.put("heap-commit", FormatUtils.formatByte(mxb.getHeapMemoryUsage().getCommitted()));
        jvm.put("heap-used", FormatUtils.formatByte(mxb.getHeapMemoryUsage().getUsed()));
        jvm.put("no_heap-max", FormatUtils.formatByte(mxb.getNonHeapMemoryUsage().getMax()));
        jvm.put("no_heap-init", FormatUtils.formatByte(mxb.getNonHeapMemoryUsage().getInit()));
        jvm.put("no_heap-commit", FormatUtils.formatByte(mxb.getNonHeapMemoryUsage().getCommitted()));
        jvm.put("no_heap-used", FormatUtils.formatByte(mxb.getNonHeapMemoryUsage().getUsed()));
        return jvm;
    }

    /**
     * 获取cpu样本值
     *
     * @return {@link ObjectNode}
     */
    ObjectNode scrapeCpu();
}
