package io.github.quickmsg.common.metric;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author luxurong
 */
public abstract class AbstractMetricRegistry implements MetricRegistry {


    private final Map<CounterType, MetricCounter> metricCounterMap = new HashMap<>();

    protected AbstractMetricRegistry(List<MetricCounter> metricCounters) {
        metricCounters.forEach(metricCounter -> metricCounterMap.put(metricCounter.getCounterType(), metricCounter));
    }

    @Override
    public MetricCounter getMetricCounter(CounterType counterType) {
        return metricCounterMap.get(counterType);
    }
}
