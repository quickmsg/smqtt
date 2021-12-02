package io.github.quickmsg.metric.api;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author luxurong
 */
public abstract class WholeCounter implements MetricCounter {

    private final AtomicLong count = new AtomicLong();

    private final MetricBean metricBean;

    protected WholeCounter(MetricBean metricBean) {
        this.metricBean = metricBean;
    }

    @Override
    public MetricBean getMetricBean() {
        return this.metricBean;
    }

    @Override
    public void increment(int index) {
        callMeter(count.getAndAdd(index) + index);
    }

    @Override
    public void decrement() {
        callMeter(count.decrementAndGet());
    }

    @Override
    public void reset() {
        count.set(0);
    }


    @Override
    public long getCounter() {
        return count.get();
    }


}
