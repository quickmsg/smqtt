package io.github.quickmsg.persistent;

import org.jooq.DSLContext;

import java.sql.Connection;

/**
 * mysql DSLContext
 *
 * @author zhaopeng
 * @date 2021/06/03
 */
public interface DbConnection {

    /**
     * 获取连接
     *
     * @return {@link Connection}
     */
    Connection getConnection();

    /**
     * 获取DSLContext
     *
     * @return {@link DSLContext}
     */
    DSLContext getDSLContext(Connection connection);
}
