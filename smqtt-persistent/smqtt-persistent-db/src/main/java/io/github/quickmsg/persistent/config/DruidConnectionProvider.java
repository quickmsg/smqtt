package io.github.quickmsg.persistent.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

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
    public Mono<Void> init(Properties properties) {
        return Mono.fromRunnable(() -> {
            if (startUp.compareAndSet(0, 1)) {
                try {
                    this.druidDataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public Mono<Connection> getConnection() {
        try {
            return Mono.just(druidDataSource.getConnection());
        } catch (SQLException e) {
            log.error("getConnection error", e);
            return Mono.empty();
        }
    }

    @Override
    public Mono<Void> shutdown() {
        return Mono.fromRunnable(() -> druidDataSource.close());
    }

}