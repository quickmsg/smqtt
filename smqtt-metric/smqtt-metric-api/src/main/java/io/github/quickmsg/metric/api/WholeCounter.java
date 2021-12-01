package io.github.quickmsg.metric.api;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author luxurong
 */
public abstract class WholeCounter implements MetricCounter {

    private AtomicInteger count =  new AtomicInteger();

    @Override
    public int getCounter() {
        return count.get();
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

}
