package io.github.quickmsg.common.metric;

/**
 * 度量
 *
 * @author easy
 * @date 2021/11/12
 */
public interface Metric {

    /**
     * 初始化
     */
    void init();

    /**
     * 获取计数器
     *
     * @param counterEnum 类型枚举
     * @return {@link MetricCounter}
     */
    MetricCounter getMetricCounter(CounterEnum counterEnum);

    /**
     * 样本数据手机
     *
     * @return {@link String}
     */
    String scrape();


}
