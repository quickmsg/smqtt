package io.github.quickmsg.common.utils;

import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;

/**
 * @author luxurong
 */
public class LoggerLevel {


    /**
     * 二进制日志级别
     */
    public static void wiretap() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.getLogger("reactor.netty").setLevel(ch.qos.logback.classic.Level.DEBUG);
    }

    /**
     * 修改root日志级别
     *
     * @param level 日志级别
     */
    public static void root(ch.qos.logback.classic.Level level) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.getLogger("root").setLevel(level);
    }


    /**
     * 修改logger日志级别
     *
     * @param loggerName 日志名称
     * @param level      日志级别
     */
    public static void logger(String loggerName, ch.qos.logback.classic.Level level) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.getLogger(loggerName).setLevel(level);
    }


}
