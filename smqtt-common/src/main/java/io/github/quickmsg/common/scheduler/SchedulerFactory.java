package io.github.quickmsg.common.scheduler;

import reactor.core.scheduler.Scheduler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author luxurong
 */
public interface SchedulerFactory {

    Map<String, Scheduler> topicSchedulers = new ConcurrentHashMap<>();


    /**
     * 获取Scheduler
     *
     * @param topic 主题
     * @return 定时器
     */
    Scheduler schedulerForTopic(String topic);


    /**
     * 构建Scheduler
     *
     * @param topic     主题
     * @param scheduler Scheduler
     */
    void buildSchedulerForTopic(String topic, Scheduler scheduler);


}
