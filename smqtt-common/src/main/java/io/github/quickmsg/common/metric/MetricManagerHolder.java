package io.github.quickmsg.common.metric;

import lombok.Getter;

import java.util.Optional;

/**
 * @author luxurong
 */
@Getter
public class MetricManagerHolder {

    public static MetricManager metricManager = null;

    public static MetricManager setMetricManager(MetricManager metricManager){
        MetricManagerHolder.metricManager = metricManager;
        return metricManager;
    }

    public static Optional<MetricRegistry> getMetricRegistry(){
        return Optional.of(metricManager).map(MetricManager::getMetricRegistry);
    }

    public static MetricManager getMetricManager(){
        return metricManager;
    }

}
