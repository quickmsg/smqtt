package io.github.quickmsg.persistent;

import io.github.quickmsg.persistent.config.DruidConnection;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author luxurong
 */
@Slf4j
public class DbConnectionHolder {

    /**
     * 获取DSLContext
     *
     * @return {@link Connection}
     */
    public static Connection getConnection() throws SQLException {
        return DruidConnection.getInstance().getDataSource().getConnection();
    }

    /**
     * 获取DSLContext
     *
     * @return {@link DSLContext}
     */
    public DSLContext getDslContext(Connection connection) {
        DSLContext dslContext = DSL.using(connection);
        return dslContext;
    }

}
