package io.github.quickmsg.source.db.config;


import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author zhaopeng
 */
@Slf4j
public class DruidConnectionProvider implements ConnectionProvider {

    private DruidConnectionProvider() {
    }

    private static DruidConnectionProvider connectionProvider = new DruidConnectionProvider();

    public static DruidConnectionProvider singleTon() {
        return connectionProvider;
    }

    private DruidDataSource druidDataSource;

    private AtomicInteger startUp = new AtomicInteger(0);

    @Override
    public void init(Properties properties) {
        if (startUp.compareAndSet(0, 1)) {
            try {
                this.druidDataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Connection getConnection() {
        try {
            return druidDataSource.getConnection();
        } catch (SQLException e) {
            log.error("getConnection error", e);
            return null;
        }
    }

    @Override
    public void shutdown() {
        if (druidDataSource != null) {
            druidDataSource.close();
        }
    }

}