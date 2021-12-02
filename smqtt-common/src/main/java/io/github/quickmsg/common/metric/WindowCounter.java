package io.github.quickmsg.common.metric;

import reactor.core.scheduler.Scheduler;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author luxurong
 */
public abstract class WindowCounter implements MetricCounter, Runnable {

    private final LongAdder sumCountAdder = new LongAdder();

    private final LongAdder windowCountAdder = new LongAdder();


    private final MetricBean metricBean;


    @Override
    public MetricBean getMetricBean() {
        return this.metricBean;
    }

    public WindowCounter(MetricBean metricBean, Integer time, TimeUnit timeUnit, Scheduler scheduler) {
        this.metricBean = metricBean;
        scheduler.schedulePeriodically(this, time, time, timeUnit);
        scheduler.start();
    }

    @Override
    public void reset() {
        sumCountAdder.reset();
        windowCountAdder.reset();
    }

    public void run() {
        windowCountAdder.reset();
    }

    @Override
    public long getCounter() {
        return this.sumCountAdder.sum();
    }


    public long getAllCount() {
        return this.sumCountAdder.sum();
    }


    @Override
    public void increment() {
        sumCountAdder.increment();
        callMeter(sumCountAdder.sum());
    }

    @Override
    public void decrement() {
        throw new UnsupportedOperationException("WindowCounter not support decrement");
    }


    public long getWindowCount() {
        return windowCountAdder.sum();
    }


}
