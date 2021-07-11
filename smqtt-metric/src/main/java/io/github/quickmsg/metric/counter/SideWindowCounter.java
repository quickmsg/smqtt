package io.github.quickmsg.metric.counter;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author luxurong
 */
@Slf4j
public class SideWindowCounter implements WindowCounter, Runnable {

    private LongAdder allAdder = new LongAdder();

    private LongAdder windowAdder = new LongAdder();

    private Sinks.Many<Long> sinks = Sinks.many().multicast().onBackpressureBuffer();

    private String windowName;

    public SideWindowCounter(Integer time, TimeUnit timeUnit, String threadName) {
        this(time, timeUnit, Schedulers.newSingle(threadName));
        windowName = threadName;
    }

    public SideWindowCounter(Integer time, TimeUnit timeUnit, Scheduler scheduler) {
        scheduler.schedulePeriodically(this, 0L, time, timeUnit);
        scheduler.start();
        if (log.isDebugEnabled()) {
            Flux.interval(Duration.ofSeconds(10))
                    .subscribe(interval -> {
                        log.debug("request window {}  size {}", windowName,windowAdder.sum());
                        log.debug("request window {}  size {}",windowName, allAdder.sum());
                    });
        }
    }


    @Override
    public void run() {
        long sum = windowAdder.sum();
        sinks.tryEmitNext(sum);
        allAdder.add(sum);
        windowAdder = new LongAdder();
    }

    @Override
    public Long intervalCount() {
        return windowAdder.sum();
    }

    @Override
    public void apply(Integer request) {
        windowAdder.add(request);
    }

    @Override
    public Long allCount() {
        return allAdder.sum();
    }

    @Override
    public Flux<Long> interval() {
        return sinks.asFlux();
    }
}
