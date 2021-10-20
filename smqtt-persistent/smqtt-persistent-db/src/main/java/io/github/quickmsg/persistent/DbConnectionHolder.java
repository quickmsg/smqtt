package io.github.quickmsg.persistent;

import io.github.quickmsg.persistent.config.HikariCPConnectionProvider;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import reactor.core.publisher.Mono;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

/**
 * @author luxurong
 */
@Slf4j
public class DbConnectionHolder {

    /**
     * 获取DSLContext
     *
     * @return {@link Mono}
     */
    public static Mono<Connection> getConnection() throws SQLException {
        Connection connection = HikariCPConnectionProvider.singleTon().getConnection();
        return Optional.ofNullable(connection).map(Mono::just).orElse(Mono.empty());
    }

    /**
     * 获取DSLContext
     *
     * @return {@link Mono}
     */
    public static Mono<DSLContext> getDslContext() throws SQLException {
        return getConnection().map(DSL::using);
    }

}
