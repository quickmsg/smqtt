package io.github.quickmsg.metric.api;

import reactor.core.scheduler.Scheduler;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author luxurong
 */
public abstract class WindowCounter implements MetricCounter, Runnable {

    private final LongAdder sumCountAdder = new LongAdder();

    private final LongAdder windowCountAdder = new LongAdder();

    private volatile long windowSize = 0L;

    public WindowCounter(Integer time, TimeUnit timeUnit, Scheduler scheduler) {
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
    public void increment(int size) {
        sumCountAdder.add(size);
    }

    @Override
    public void decrement() {
        throw new UnsupportedOperationException("WindowCounter not support decrement");
    }


    public long getWindowCount() {
        return  windowCountAdder.sum();
    }


}
