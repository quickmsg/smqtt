package io.github.quickmsg.common.config;

import io.github.quickmsg.common.acl.AclPolicy;
import lombok.Data;

/**
 * @author luxurong
 */
@Data
public class AclConfig {

    private AclPolicy aclPolicy;

    private String filePath;

    private JdbcAclConfig jdbcAclConfig;

    @Data
    public static class JdbcAclConfig {

        private String driver;

        private String url;

        private String username;

        private String password;

    }
}
