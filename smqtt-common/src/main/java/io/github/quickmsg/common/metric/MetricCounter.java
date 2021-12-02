package io.github.quickmsg.common.metric;

/**
 * @author luxurong
 */
public interface MetricCounter {

    long getCounter();

    void increment(int size);

    void decrement();

    void reset();

    void callMeter(long counter);

    MetricBean getMetricBean();

    CounterType getCounterType();





}
