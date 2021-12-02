package io.github.quickmsg.metric.api;

import reactor.core.scheduler.Schedulers;

import java.util.concurrent.TimeUnit;

/**
 * @author luxurong
 */
public class WriteCounter extends WindowCounter {

    public WriteCounter() {
        super(1, TimeUnit.SECONDS, Schedulers.newSingle("read"));
    }


    @Override
    public void callMeter(long counter) {

    }
}
