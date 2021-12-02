package io.github.quickmsg.metric;

import io.github.quickmsg.metric.api.*;
import io.micrometer.core.instrument.Measurement;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Statistic;

import java.util.stream.StreamSupport;

/**
 * @author luxurong
 */
public class InfluxDbMetricRegistry implements MetricRegistry {

    private final MetricBean metricBean;

    public InfluxDbMetricRegistry(MetricBean metricBean) {
        this.metricBean = metricBean;
    }


    @Override
    public WholeCounter getTopicCounter() {
        return new TopicCounter(metricBean);
    }

    @Override
    public WholeCounter getConnectCounter() {
        return new ConnectCounter(metricBean);
    }

    @Override
    public WindowCounter getReadCounter() {
        return new ReadCounter(metricBean);
    }

    @Override
    public WindowCounter getWriteCounter() {
        return new WriteCounter(metricBean);
    }

    @Override
    public Object getMetricKey(Meter.Id id, Statistic statistic) {
        MeterRegistry metricRegistry = metricBean.getMeterRegistry();
        return metricRegistry
                .getMeters()
                .stream()
                .filter(meter -> meter.getId().equals(id))
                .flatMap(meter -> StreamSupport.stream(meter.measure().spliterator(), false))
                .filter(measurement -> measurement.getStatistic() == statistic)
                .map(Measurement::getValue)
                .reduce(Double::sum).orElse(0D);
    }
}
