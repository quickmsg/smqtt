package io.github.quickmsg.metric.counter;

import io.github.quickmsg.common.metric.MetricConstant;
import io.github.quickmsg.common.metric.MetricCounter;
import io.github.quickmsg.metric.Influx1Metric;

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


    static {
        Influx1Metric.INFLUX_METER_REGISTRY_INSTANCE.gauge(MetricConstant.TOPIC_COUNTER_NAME, ATOMICINTEGER);
    }

    @Override
    public void increment() {
        Influx1Metric.INFLUX_METER_REGISTRY_INSTANCE.gauge(MetricConstant.TOPIC_COUNTER_NAME, ATOMICINTEGER.incrementAndGet());
    }

    @Override
    public void decrement() {
        Influx1Metric.INFLUX_METER_REGISTRY_INSTANCE.gauge(MetricConstant.TOPIC_COUNTER_NAME, ATOMICINTEGER.decrementAndGet());
    }

}
