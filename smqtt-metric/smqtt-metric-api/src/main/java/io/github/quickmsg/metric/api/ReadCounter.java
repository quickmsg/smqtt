package io.github.quickmsg.metric.api;

import reactor.core.scheduler.Schedulers;

import java.util.concurrent.TimeUnit;

/**
 * @author luxurong
 */
public class ReadCounter extends WindowCounter {

    public ReadCounter() {
        super(1, TimeUnit.SECONDS, Schedulers.newSingle("read"));
    }


    @Override
    public void callMeter(long counter) {

    }
}
