package io.github.quickmsg.metric.api;

import reactor.core.scheduler.Schedulers;

import java.util.concurrent.TimeUnit;

/**
 * @author luxurong
 */
public class WriteCounter extends WindowCounter {

    private final MetricBean metricBean;


    @Override
    public MetricBean getMetricBean() {
        return this.metricBean;
    }

    public WriteCounter(MetricBean metricBean) {
        super(metricBean,1, TimeUnit.SECONDS, Schedulers.newSingle("read"));
        this.metricBean = metricBean;
    }


    @Override
    public void callMeter(long counter) {

    }


}
