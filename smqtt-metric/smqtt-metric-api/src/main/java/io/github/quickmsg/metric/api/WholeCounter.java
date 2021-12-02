package io.github.quickmsg.metric.api;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author luxurong
 */
public abstract class WholeCounter implements MetricCounter {

    private final AtomicLong count = new AtomicLong();

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
