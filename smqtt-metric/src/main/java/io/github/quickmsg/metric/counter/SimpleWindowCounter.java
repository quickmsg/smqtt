package io.github.quickmsg.metric.counter;

import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.LongAdder;

/**
 * @author luxurong
 * @date 2021/7/7 11:41
 * @description
 */
public class SimpleWindowCounter implements WindowCounter {


    private LongAdder windowAdder = new LongAdder();

    @Override
    public Long intervalCount() {
        return windowAdder.sum();
    }

    @Override
    public Long allCount() {
        return windowAdder.sum();
    }

    @Override
    public void apply(Integer request) {
        windowAdder.add(request);
    }

    @Override
    public Flux<Long> interval() {
        return Flux.empty();
    }
}
