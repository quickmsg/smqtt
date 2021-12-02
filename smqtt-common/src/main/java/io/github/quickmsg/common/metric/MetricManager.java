package io.github.quickmsg.common.metric;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.quickmsg.common.config.BootstrapConfig;
import io.github.quickmsg.common.utils.FormatUtils;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Statistic;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.util.Util;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.text.DecimalFormat;
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

    SystemInfo systemInfo = new SystemInfo();

    int O_SHI_WAIT_SECOND = 500;



    default Map<String, Object> getCpuMetric() {
        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        Map<String, Object> metrics = new HashMap<>();
        CentralProcessor processor = hardware.getProcessor();
        // CPU信息
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        Util.sleep(O_SHI_WAIT_SECOND);
        long[] ticks = processor.getSystemCpuLoadTicks();
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;
        //cpu核数
        metrics.put("cpuNum", processor.getLogicalProcessorCount());
        //cpu系统使用率
        metrics.put("cSys", new DecimalFormat("#.##%").format(cSys * 1.0 / totalCpu));
        //cpu用户使用率
        metrics.put("user", new DecimalFormat("#.##%").format(user * 1.0 / totalCpu));
        //cpu当前等待率
        metrics.put("iowait", new DecimalFormat("#.##%").format(iowait * 1.0 / totalCpu));
        //cpu当前使用率
        metrics.put("idle", new DecimalFormat("#.##%").format(1.0 - (idle * 1.0 / totalCpu)));
        //cpu核数
        metrics.put("cpuNum", Runtime.getRuntime().availableProcessors());
        return metrics;
    }

    default Map<String, Object> getBufferMetric() {
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
