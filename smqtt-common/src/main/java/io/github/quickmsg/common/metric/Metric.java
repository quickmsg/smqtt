package io.github.quickmsg.common.metric;

/**
 * 度量
 *
 * @author easy
 * @date 2021/11/12
 */
public interface Metric {

    void init();

    MetricCounter getMetricCounter(String name);

    String scrape();


}
