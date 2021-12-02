package io.github.quickmsg.metric;

import io.github.quickmsg.common.config.BootstrapConfig;
import io.github.quickmsg.common.metric.*;
import io.github.quickmsg.common.metric.counter.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author luxurong
 */
public class InfluxDbMetricManager implements MetricManager {

    private final BootstrapConfig.MeterConfig config;

    private final MetricBean metricBean;

    private final MetricRegistry metricRegistry;

    public InfluxDbMetricManager(BootstrapConfig.MeterConfig config) {
        this.config = config;
        this.metricBean = new InfluxDbMetricBean(this.config);
        this.metricRegistry = createMetricRegistry(metricBean);
    }

    private MetricRegistry createMetricRegistry(MetricBean metricBean) {
        List<MetricCounter> metricCounters = new ArrayList<>();
        metricCounters.add(new WriteCounter(metricBean));
        metricCounters.add(new ConnectCounter(metricBean));
        metricCounters.add(new SubscribeCounter(metricBean));
        metricCounters.add(new UnSubscribeCounter(metricBean));
        metricCounters.add(new PublishCounter(metricBean));
        metricCounters.add(new DisConnectCounter(metricBean));
        metricCounters.add(new ReadCounter(metricBean));
        metricCounters.add(new WriteCounter(metricBean));
        return new InfluxDbMetricRegistry(metricCounters);
    }

    @Override
    public MetricRegistry getMetricRegistry() {
        return this.metricRegistry;
    }

    @Override
    public MetricBean getMetricBean() {
        return this.metricBean;
    }

    @Override
    public BootstrapConfig.MeterConfig getMeterConfig() {
        return this.config;
    }


}
