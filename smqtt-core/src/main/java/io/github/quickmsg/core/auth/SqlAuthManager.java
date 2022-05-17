package io.github.quickmsg.core.auth;

import io.github.quickmsg.common.auth.AuthManager;
import io.github.quickmsg.common.config.AuthConfig;
import io.github.quickmsg.source.db.config.HikariCPConnectionProvider;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author luxurong
 */
@Slf4j
public class SqlAuthManager implements AuthManager {

    private AuthConfig authConfig;

    public SqlAuthManager(AuthConfig authConfig) {
        this.authConfig = authConfig;
        // 初始化数据库连接池
        Properties properties = new Properties();
        properties.put("jdbcUrl", authConfig.getSql().getUrl());
        properties.put("username", authConfig.getSql().getUsername());
        properties.put("password", authConfig.getSql().getPassword());

        HikariCPConnectionProvider
                .singleTon()
                .init(properties);
    }

    public Boolean auth(String userName, byte[] passwordInBytes, String clientIdentifier) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = HikariCPConnectionProvider.singleTon().getConnection();
            ps = conn.prepareStatement(authConfig.getSql().getAuthSql());
            ps.setString(1, userName);
            ps.setString(2, new String(passwordInBytes, StandardCharsets.UTF_8));
            ps.setString(3, clientIdentifier);

            rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            log.error("auth error clientIdentifier={}", clientIdentifier, e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                log.error("close error clientIdentifier={}", clientIdentifier, e);
            }
        }

        return false;
    }
}
