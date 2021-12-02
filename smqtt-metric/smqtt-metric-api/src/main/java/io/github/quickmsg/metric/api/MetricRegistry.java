package io.github.quickmsg.metric.api;


import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Statistic;

/**
 * @author luxurong
 */
public interface MetricRegistry {


    WholeCounter getTopicCounter();


    WholeCounter getConnectCounter();


    WindowCounter getReadCounter();


    WindowCounter getWriteCounter();

    Object getMetricKey(Meter.Id id, Statistic statistic);


}
