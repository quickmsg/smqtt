package io.github.quickmsg.common.metric;

/**
 * 度量计数器
 *
 * @author easy
 * @date 2021/11/13
 */
public interface MetricCounter {

    /**
     * 增加数
     */
    void increment();

    /**
     * 减少数
     */
    void decrement();
}
