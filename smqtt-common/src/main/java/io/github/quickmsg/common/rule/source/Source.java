package io.github.quickmsg.common.rule.source;

/**
 * @author luxurong
 */
public enum Source {
    /**
     * kafka
     */
    KAFKA,
    /**
     * rocketMq
     */
    ROCKET_MQ,
    /**
     * RABBIT_MQ
     */
    RABBIT_MQ,
    /**
     * 数据库
     */
    DATA_BASE,
    /**
     * hBase
     */
    H_BASE,

    /**
     * http
     */
    HTTP,
    /**
     * mqtt转发
     */
    MQTT
}
