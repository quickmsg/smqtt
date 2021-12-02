package io.github.quickmsg.common.metric;

import reactor.core.scheduler.Schedulers;

import java.util.concurrent.TimeUnit;

/**
 * @author luxurong
 */
public class ReadCounter extends WindowCounter {

    public ReadCounter(MetricBean metricBean) {
        super(metricBean, 1, TimeUnit.SECONDS, Schedulers.newSingle("read"));
    }


    @Override
    public void callMeter(long counter) {

    }
}
