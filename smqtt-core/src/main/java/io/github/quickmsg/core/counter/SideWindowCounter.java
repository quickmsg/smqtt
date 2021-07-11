package io.github.quickmsg.core.counter;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author luxurong
 */
@Slf4j
public class SideWindowCounter implements WindowCounter, Runnable {

    private LongAdder longAdder = new LongAdder();

    private Sinks.Many<Long> sinks = Sinks.many().multicast().onBackpressureBuffer();

    public SideWindowCounter(Integer time, TimeUnit timeUnit, String threadName) {
        this(+time, timeUnit, Schedulers.newSingle(threadName));
    }

    public SideWindowCounter(Integer time, TimeUnit timeUnit, Scheduler scheduler) {
        scheduler.schedulePeriodically(this, 0L, time, timeUnit);
        scheduler.start();
    }


    @Override
    public void run() {
        long sum = longAdder.sum();
        log.info("request buffer size {}", sum);
        sinks.tryEmitNext(sum);
        longAdder = new LongAdder();
    }

    @Override
    public Long count() {
        return longAdder.sum();
    }

    @Override
    public void apply(Integer request) {
        longAdder.add(request);
    }

    @Override
    public Flux<Long> interval() {
        return sinks.asFlux();
    }
}
