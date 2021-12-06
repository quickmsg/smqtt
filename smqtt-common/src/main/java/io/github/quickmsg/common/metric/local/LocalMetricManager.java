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
    public MetricBean getMetricBean() {
        return new LocalMetricBean();
    }

    @Override
    public BootstrapConfig.MeterConfig getMeterConfig() {
        return null;
    }
}
