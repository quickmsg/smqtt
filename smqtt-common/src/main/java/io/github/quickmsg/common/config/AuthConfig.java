package io.github.quickmsg.common.config;

import lombok.Data;

import java.util.Map;

/**
 * @author luxurong
 */
@Data
public class AuthConfig {

    private SqlAuthConfig sql;

    private HttpAuthConfig http;

    private FixedAuthConfig fixed;

    private String file;

    @Data
    public static class FixedAuthConfig {

        private String username;

        private String password;

    }

    @Data
    public static class HttpAuthConfig {

        private String host;

        private int port;

        private String path;

        private String method;

        private Map<String,String> headers;

        private Map<String,String> params;

    }


    @Data
    public static class SqlAuthConfig {

        private String driver;

        private String url;

        private String username;

        private String password;

        private String authSql;

    }
}
