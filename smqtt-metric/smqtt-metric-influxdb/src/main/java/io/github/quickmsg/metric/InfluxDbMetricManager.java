package io.github.quickmsg.metric;

import io.github.quickmsg.common.config.BootstrapConfig;
import io.github.quickmsg.common.metric.*;

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
        this.metricRegistry = new InfluxDbMetricRegistry(createMetricRegistry(metricBean));

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
