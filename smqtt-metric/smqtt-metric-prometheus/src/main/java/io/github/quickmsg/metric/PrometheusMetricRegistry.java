package io.github.quickmsg.metric;

import io.github.quickmsg.common.metric.AbstractMetricRegistry;
import io.github.quickmsg.common.metric.MetricCounter;

import java.util.List;

/**
 * @author luxurong
 */
public class PrometheusMetricRegistry  extends AbstractMetricRegistry {

    protected PrometheusMetricRegistry(List<MetricCounter> metricCounters) {
        super(metricCounters);
    }
}
