package io.github.quickmsg.metric.api;

/**
 * @author luxurong
 */
public interface MetricCounter {

    long getCounter();

    void increment(int size);

    void decrement();

    void reset();

    void callMeter(long counter);





}
