package io.github.quickmsg.common.metric;

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

    public void initCount() {
        getMetricBean().getMeterRegistry().gauge(getCounterType().getDesc(), count);
    }

    @Override
    public MetricBean getMetricBean() {
        return this.metricBean;
    }

    @Override
    public void increment() {
        callMeter(count.incrementAndGet());
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
