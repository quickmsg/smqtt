package io.github.quickmsg.core.auth;

import io.github.quickmsg.common.auth.AuthManager;
import io.github.quickmsg.common.config.AuthConfig;

/**
 * @author luxurong
 */
public class FixedAuthManager implements AuthManager {

    private final AuthConfig authConfig;

    public FixedAuthManager(AuthConfig authConfig) {
        this.authConfig = authConfig;
    }

    @Override
    public Boolean auth(String userName, byte[] passwordInBytes, String clientIdentifier) {
        return authConfig.getFixed().getUsername().equals(userName)
                && authConfig.getFixed().getPassword().equals(new String(passwordInBytes));
    }
}
