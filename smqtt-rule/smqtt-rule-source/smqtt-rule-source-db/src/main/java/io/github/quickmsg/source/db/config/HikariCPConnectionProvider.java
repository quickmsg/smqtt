package io.github.quickmsg.source.db.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author zhaopeng
 */
@Slf4j
public class HikariCPConnectionProvider implements ConnectionProvider {

    private HikariCPConnectionProvider() {
    }

    private static HikariCPConnectionProvider connectionProvider = new HikariCPConnectionProvider();

    public static HikariCPConnectionProvider singleTon() {
        return connectionProvider;
    }

    private HikariDataSource hikariDataSource;

    private AtomicInteger startUp = new AtomicInteger(0);

    @Override
    public void init(Properties properties) {
        if (startUp.compareAndSet(0, 1)) {
            try {
                HikariConfig config = new HikariConfig(properties);
                this.hikariDataSource = new HikariDataSource(config);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Connection getConnection() {
        try {
            return hikariDataSource.getConnection();
        } catch (SQLException e) {
            log.error("getConnection error", e);
            return null;
        }
    }

    @Override
    public void shutdown() {
        if (hikariDataSource != null) {
            hikariDataSource.close();
        }
    }

}