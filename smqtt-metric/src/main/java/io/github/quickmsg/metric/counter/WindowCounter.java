package io.github.quickmsg.metric.counter;

import reactor.core.publisher.Flux;

/**
 * @author luxurong
 */
public interface WindowCounter {

    /**
     * 周期汇总
     *
     * @return count
     */
    Long intervalCount();

    /**
     * 汇总
     *
     * @return count
     */
    Long allCount();


    /**
     * 添加汇总值
     *
     * @param request 一次请求的数
     */
    void apply(Integer request);


    /**
     * 周期性获取统计值
     *
     * @return {@link Flux}
     */
    Flux<Long> interval();

}

