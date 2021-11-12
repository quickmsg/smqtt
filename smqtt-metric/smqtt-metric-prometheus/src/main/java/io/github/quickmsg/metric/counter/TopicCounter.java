package io.github.quickmsg.metric.counter;

import io.github.quickmsg.common.metric.MetricCounter;
import io.github.quickmsg.metric.PrometheusMetric;
import io.prometheus.client.exporter.common.TextFormat;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * 主题计数器
 *
 * @author easy
 * @date 2021/11/12
 */
public class TopicCounter implements MetricCounter {

    public static final TopicCounter TOPIC_COUNTER_INSTANCE = new TopicCounter();

    private static final AtomicInteger  ATOMICINTEGER= new AtomicInteger(0);

    public static final String NAME = "smqtt.topic.count";

    static {
        PrometheusMetric.PROMETHEUS_METER_REGISTRY_INSTANCE.gauge(NAME, ATOMICINTEGER);
    }

    public void increment() {
        PrometheusMetric.PROMETHEUS_METER_REGISTRY_INSTANCE.gauge(NAME, ATOMICINTEGER.incrementAndGet());
    }

    public void decrement() {
        PrometheusMetric.PROMETHEUS_METER_REGISTRY_INSTANCE.gauge(NAME, ATOMICINTEGER.decrementAndGet());
    }

}
