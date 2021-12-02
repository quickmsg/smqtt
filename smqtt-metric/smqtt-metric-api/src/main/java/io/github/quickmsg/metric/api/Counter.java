package io.github.quickmsg.metric.api;

/**
 * @author luxurong
 */
public interface Counter {

    void increment(int size);

    void decrement(int size);

    long  getCounter();


}
