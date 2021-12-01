package io.github.quickmsg.metric.api;


/**
 * @author luxurong
 */
public interface MetricFactory {

    MetricBean getMetricBean(MetricRegistry metricRegistry);


}
