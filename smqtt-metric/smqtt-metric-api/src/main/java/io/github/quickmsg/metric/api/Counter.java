package io.github.quickmsg.metric.api;

/**
 * @author luxurong
 */
public interface Counter {

    void increment();

    void decrement();

    int  getCounter();


}
