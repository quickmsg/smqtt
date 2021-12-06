package io.github.quickmsg.common.metric.local;

import io.github.quickmsg.common.config.BootstrapConfig;
import io.github.quickmsg.common.metric.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author luxurong
 */
public class LocalMetricManager implements MetricManager {

    public LocalMetricManager() {
    }


    @Override
    public MetricRegistry getMetricRegistry() {
        return new AbstractMetricRegistry(createMetricRegistry(getMetricBean())) {
        };
    }

    @Override
    public List<MetricCounter> createMetricRegistry(MetricBean metricBean) {
        List<MetricCounter> metricCounters = new ArrayList<>();
        metricCounters.add(new EventCounter(metricBean, CounterType.CONNECT_EVENT));
        metricCounters.add(new EventCounter(metricBean, CounterType.PUBLISH_EVENT));
        metricCounters.add(new EventCounter(metricBean, CounterType.SUBSCRIBE_EVENT));
        metricCounters.add(new EventCounter(metricBean, CounterType.UN_SUBSCRIBE_EVENT));
        metricCounters.add(new EventCounter(metricBean, CounterType.DIS_CONNECT_EVENT));
        metricCounters.add(new EventCounter(metricBean, CounterType.CLOSE_EVENT));
        metricCounters.add(new TotalCounter(metricBean, CounterType.CONNECT));
        metricCounters.add(new TotalCounter(metricBean, CounterType.SUBSCRIBE));
        return metricCounters;
    }


    @Override
    public MetricBean getMetricBean() {
        return null;
    }

    @Override
    public BootstrapConfig.MeterConfig getMeterConfig() {
        return null;
    }
}
