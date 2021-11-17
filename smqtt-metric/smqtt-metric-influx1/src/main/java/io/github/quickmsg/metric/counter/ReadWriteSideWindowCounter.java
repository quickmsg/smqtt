package io.github.quickmsg.metric.counter;

import io.github.quickmsg.common.metric.MetircConstant;
import io.github.quickmsg.common.metric.Metric;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Statistic;
import io.micrometer.core.instrument.Tags;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.DoubleAdder;

@Slf4j
public class ReadWriteSideWindowCounter implements Runnable {

    /**
     * 读取值
     */
    private DoubleAdder LAST_READ_SIZE = new DoubleAdder();
    /**
     * 写入值
     */
    private DoubleAdder LAST_WRITE_SIZE = new DoubleAdder();
    /**
     * 线程名称
     */
    private String threadName;

    private Metric metric;


    private static volatile ReadWriteSideWindowCounter instance = null;

    public static ReadWriteSideWindowCounter getInstance(Integer time, TimeUnit timeUnit, String threadName, Metric metric) {
        if (instance == null) {
            synchronized (ReadWriteSideWindowCounter.class) {
                if (instance == null) {
                    instance = new ReadWriteSideWindowCounter(time, timeUnit, threadName, metric);
                }
            }
        }
        return instance;
    }


    private ReadWriteSideWindowCounter(Integer time, TimeUnit timeUnit, String threadName, Metric metric) {
        this(time, timeUnit, Schedulers.newSingle(threadName));
        this.threadName = threadName;
        this.metric = metric;
    }

    private ReadWriteSideWindowCounter(Integer time, TimeUnit timeUnit, Scheduler scheduler) {
        scheduler.schedulePeriodically(this, 0L, time, timeUnit);
        scheduler.start();
        if (log.isDebugEnabled()) {
            Flux.interval(Duration.ofSeconds(10))
                    .subscribe(interval -> {
                        log.debug("name {}  size {}", "LAST_READ_SIZE", LAST_READ_SIZE.sum());
                        log.debug("name {}  size {}", "LAST_WRITE_SIZE", LAST_WRITE_SIZE.sum());
                    });
        }
    }

    @Override
    public void run() {
        double readSize = metric.formatValue(metric.scrapeByMeterId(new Meter.Id(MetircConstant.REACTOR_NETTY_TCP_SERVER_DATA_RECEIVED, Tags.empty(), null, null, null), Statistic.TOTAL));
        double writeSize = metric.formatValue(metric.scrapeByMeterId(new Meter.Id(MetircConstant.REACTOR_NETTY_TCP_SERVER_DATA_SENT, Tags.empty(), null, null, null), Statistic.TOTAL));

        LAST_READ_SIZE.reset();
        LAST_WRITE_SIZE.reset();

        LAST_READ_SIZE.add(readSize);
        LAST_WRITE_SIZE.add(writeSize);
    }

    public Double getLastReadSize() {
        return LAST_READ_SIZE.sum();
    }

    public Double getLastWriteSize() {
        return LAST_WRITE_SIZE.sum();
    }
}
