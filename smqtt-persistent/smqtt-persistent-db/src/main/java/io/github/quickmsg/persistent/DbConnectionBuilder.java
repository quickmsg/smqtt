package io.github.quickmsg.persistent;

import io.github.quickmsg.persistent.config.DruidConnection;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.sql.Connection;

/**
 * mysql DSLContext
 *
 * @author zhaopeng
 * @date 2021/06/03
 */
public class DbConnectionBuilder implements DbConnection {

    @Override
    public Connection getConnection() {
        return DruidConnection.getInstace().getConnection();
    }

    /**
     * 获取DSLContext
     *
     * @return {@link DSLContext}
     */
    @Override
    public DSLContext getDSLContext(Connection connection) {
        DSLContext dslContext = DSL.using(connection);
        return dslContext;
    }
}
