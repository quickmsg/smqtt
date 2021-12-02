package io.github.quickmsg.common.metric;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.quickmsg.common.config.BootstrapConfig;
import io.github.quickmsg.common.utils.FormatUtils;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Statistic;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author luxurong
 */
public interface MetricManager {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    MetricRegistry getMetricRegistry();

    MetricBean getMetricBean();

    BootstrapConfig.MeterConfig getMeterConfig();

    default Map<String,Object>  getJvmMetric() {
        Map<String,Object> metrics = new HashMap<>();
        Properties props = System.getProperties();
        ObjectMapper objectMapper = new ObjectMapper();
        MemoryMXBean mxb = ManagementFactory.getMemoryMXBean();
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
        metrics.put("smqtt", "1.1.0");
        metrics.put("start_time", sdf.format(new Date(runtimeBean.getStartTime())));
        metrics.put("jdk_home", props.getProperty("java.home"));
        metrics.put("jdk_version", props.getProperty("java.version"));
        metrics.put("thread.count", threadBean.getThreadCount());
        metrics.put("heap-max", FormatUtils.formatByte(mxb.getHeapMemoryUsage().getMax()));
        metrics.put("heap-init", FormatUtils.formatByte(mxb.getHeapMemoryUsage().getInit()));
        metrics.put("heap-commit", FormatUtils.formatByte(mxb.getHeapMemoryUsage().getCommitted()));
        metrics.put("heap-used", FormatUtils.formatByte(mxb.getHeapMemoryUsage().getUsed()));
        metrics.put("no_heap-max", FormatUtils.formatByte(mxb.getNonHeapMemoryUsage().getMax()));
        metrics.put("no_heap-init", FormatUtils.formatByte(mxb.getNonHeapMemoryUsage().getInit()));
        metrics.put("no_heap-commit", FormatUtils.formatByte(mxb.getNonHeapMemoryUsage().getCommitted()));
        metrics.put("no_heap-used", FormatUtils.formatByte(mxb.getNonHeapMemoryUsage().getUsed()));
        return metrics;
    }

    default Map<String, Object> getCpuMetric() {
        Map<String, Object> metrics = new HashMap<>();
        //cpu核数
        metrics.put("cpuNum", Runtime.getRuntime().availableProcessors());
        //cpu系统使用率
        metrics.put("cSys", getMetricRegistry().getMetricKey(new Meter.Id(MetricConstant.SYSTEM_CPU_USAGE, getMetricBean().getTags(), null, "cpu usr", Meter.Type.GAUGE), Statistic.VALUE));
        //cpu用户使用率
        metrics.put("user",  getMetricRegistry().getMetricKey(new Meter.Id(MetricConstant.PROCESS_CPU_USAGE, getMetricBean().getTags(), null, "cpu usr", Meter.Type.GAUGE), Statistic.VALUE));
        // 守护线程数
        metrics.put("daemonThreads",  getMetricRegistry().getMetricKey(new Meter.Id(MetricConstant.JVM_THREADS_DAEMON_THREADS, getMetricBean().getTags(), null, "cpu usr", Meter.Type.GAUGE), Statistic.VALUE));
        // 最大线程数
        metrics.put("peakThreads",  getMetricRegistry().getMetricKey(new Meter.Id(MetricConstant.JVM_THREADS_PEAK_THREADS, getMetricBean().getTags(), null, "cpu usr", Meter.Type.GAUGE), Statistic.VALUE));
        return metrics;
    }

    default Map<String, Object> getRWBufferMetric() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("connect_size",  getMetricRegistry().getConnectCounter().getCounter());
        metrics.put("topic_size",  getMetricRegistry().getTopicCounter().getCounter());
        metrics.put("read_size", FormatUtils.formatByte( getMetricRegistry().getReadCounter().getAllCount()));
        metrics.put("read_hour_size", FormatUtils.formatByte( getMetricRegistry().getReadCounter().getWindowCount()));
        metrics.put("write_size", FormatUtils.formatByte( getMetricRegistry().getWriteCounter().getAllCount()));
        metrics.put("write_hour_size", FormatUtils.formatByte( getMetricRegistry().getWriteCounter().getWindowCount()));
        return metrics;
    }





}
