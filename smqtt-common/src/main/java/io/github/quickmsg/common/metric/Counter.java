package io.github.quickmsg.common.metric;

/**
 * @author luxurong
 */
public interface Counter {

    void increment(int size);

    void decrement(int size);

    long  getCounter();


}
