package io.github.quickmsg.persistent.config;

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
     */
    void init(Properties properties);

    /**
     * 获取链接
     *
     * @return {@link Connection}
     */
    Connection getConnection();


    /**
     * 关闭链接
     */
    void shutdown();


}
