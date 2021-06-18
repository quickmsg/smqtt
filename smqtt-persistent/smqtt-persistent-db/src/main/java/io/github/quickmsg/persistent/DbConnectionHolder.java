package io.github.quickmsg.persistent;

import io.github.quickmsg.persistent.config.DruidConnectionProvider;
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
     * @return {@link Mono<Connection>}
     */
    public static Mono<Connection> getConnection() throws SQLException {
        Connection connection = DruidConnectionProvider.singleTon().getConnection();
        return Optional.ofNullable(connection).map(Mono::just).orElse(Mono.empty());
    }

    /**
     * 获取DSLContext
     *
     * @return {@link Mono<DSLContext>}
     */
    public static Mono<DSLContext> getDslContext() throws SQLException {
        return getConnection().map(DSL::using);
    }

}
