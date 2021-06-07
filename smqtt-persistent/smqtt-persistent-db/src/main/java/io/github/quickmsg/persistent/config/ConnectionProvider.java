package io.github.quickmsg.persistent.config;

import reactor.core.publisher.Mono;

import java.sql.Connection;
import java.util.Properties;

/**
 * @author luxurong
 */
public interface ConnectionProvider {


    /**
     * 初始化
     *
     * @param properties 配置
     * @return {@link Mono<Void>}
     */
    Mono<Void> init(Properties properties);

    /**
     * 获取链接
     *
     * @return {@link Mono<Connection>}
     */
    Mono<Connection> getConnection();


    /**
     * 关闭链接
     *
     * @return {@link Mono<Connection>}
     */
    Mono<Void> shutdown();


}
