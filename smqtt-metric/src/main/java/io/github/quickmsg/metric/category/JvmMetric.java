package io.github.quickmsg.metric.category;

import com.alibaba.fastjson.JSONObject;
import io.github.quickmsg.metric.MetricsGetter;
import io.github.quickmsg.metric.utils.FormatUtils;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.ThreadMXBean;
import java.util.Properties;

/**
 * jvm数据
 *
 * @author luxurong
 */
public class JvmMetric implements MetricsGetter {

    public final static JvmMetric JVM_METRIC_INSTANCE = new JvmMetric();

    private JvmMetric() {

    }


    @Override
    public JSONObject metrics() {
        Properties props = System.getProperties();
        JSONObject jvm = new JSONObject();
        MemoryMXBean mxb = ManagementFactory.getMemoryMXBean();
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
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
}
