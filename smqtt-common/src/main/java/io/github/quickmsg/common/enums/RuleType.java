package io.github.quickmsg.common.enums;

/**
 * @author luxurong
 */
public enum RuleType {

    /**
     * 条件过滤器
     */
    PREDICATE,

    /**
     * topic转发器
     */
    TOPIC,
    /**
     * log转发器
     */
    LOG,
    /**
     * kafka转发器
     */
    KAFKA,
    /**
     * rocketmq转发器
     */
    ROCKET_MQ,
    /**
     * rabbitmq转发器
     */
    RABBIT_MQ,
    /**
     * web
     */
    HTTP,

    /**
     * 数据库
     */
    DATA_BASE,
    /**
     * mqtt转发
     */
    MQTT

}
