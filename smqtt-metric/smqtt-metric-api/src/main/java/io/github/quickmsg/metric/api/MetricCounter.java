package io.github.quickmsg.metric.api;

/**
 * @author luxurong
 */
public interface MetricCounter {

    int getCounter();

    void increment();

    void decrement();

    void reset();

    void callMeter(int counter);





}
