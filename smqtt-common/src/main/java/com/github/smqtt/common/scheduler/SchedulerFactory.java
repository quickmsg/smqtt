package com.github.smqtt.common.scheduler;

import reactor.core.scheduler.Scheduler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author luxurong
 * @date 2021/4/6 13:16
 * @description
 */
public interface SchedulerFactory {

    Map<String, Scheduler> topicSchedulers = new ConcurrentHashMap<>();


    /**
     * 获取Scheduler
     *
     * @param topic 主题
     * @return Scheduler
     */
    Scheduler schedulerForTopic(String topic);


    /**
     * 构建Scheduler
     *
     * @param topic     主题
     * @param scheduler Scheduler
     * @return void
     */
    void buildSchedulerForTopic(String topic, Scheduler scheduler);


}
