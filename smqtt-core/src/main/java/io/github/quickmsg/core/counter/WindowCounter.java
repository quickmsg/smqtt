package io.github.quickmsg.core.counter;

import reactor.core.publisher.Flux;

/**
 * @author luxurong
 */
public interface WindowCounter {

    /**
     * 汇总
     *
     * @return {@link Flux<Long> }
     */
    Long count();


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

