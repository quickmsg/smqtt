package io.github.quickmsg.common.metric;


/**
 * @author luxurong
 */
public interface MetricRegistry {

    MetricCounter getMetricCounter(CounterType counterType);



}
