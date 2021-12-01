package io.github.quickmsg.metric.api;

import reactor.core.scheduler.Scheduler;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author luxurong
 */
public abstract class WindowCounter implements Counter, Runnable {


    private volatile int lastCount = 0;

    private final LongAdder countAdder = new LongAdder();


    public WindowCounter(Integer time, TimeUnit timeUnit, String threadName, Scheduler scheduler) {
        scheduler.schedulePeriodically(this, time, time, timeUnit);
        scheduler.start();
    }


    public void run() {
        int nowCount = getCounter();
        countAdder.add(nowCount - lastCount);
        lastCount = nowCount;
    }


    public int getLastCount() {
        return this.lastCount;
    }


    public int getWindowCount() {
        return (int) countAdder.sum();
    }


}
