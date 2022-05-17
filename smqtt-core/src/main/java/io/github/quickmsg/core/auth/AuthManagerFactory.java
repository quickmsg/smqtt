package io.github.quickmsg.core.auth;

import io.github.quickmsg.common.auth.AuthManager;
import io.github.quickmsg.common.config.AuthConfig;

/**
 * @author luxurong
 */
public class AuthManagerFactory {

    private final AuthConfig authConfig;

    public AuthManagerFactory(AuthConfig authConfig) {
        this.authConfig = authConfig;
    }

    public AuthManager getAuthManager() {
        if (authConfig == null) {
            return new NoneAuthManager();
        }
        if (authConfig.getHttp() != null) {
            return new HttpAuthManager(authConfig);
        } else if (authConfig.getFile() != null) {
            return new FileAuthManager(authConfig);
        } else if (authConfig.getFixed() != null) {
            return new FixedAuthManager(authConfig);
        } else if (authConfig.getSql() != null) {
            return new SqlAuthManager(authConfig);
        } else {
            return new NoneAuthManager();
        }
    }
}