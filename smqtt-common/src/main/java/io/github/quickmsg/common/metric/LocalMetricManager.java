package io.github.quickmsg.common.metric;

import io.github.quickmsg.common.config.BootstrapConfig;

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
    public MetricBean getMetricBean() {
        return null;
    }

    @Override
    public BootstrapConfig.MeterConfig getMeterConfig() {
        return null;
    }
}
