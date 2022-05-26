package io.github.quickmsg.core.auth;

import io.github.quickmsg.common.auth.AuthManager;

/**
 * @author luxurong
 */
public class NoneAuthManager implements AuthManager {
    @Override
    public Boolean auth(String userName, byte[] passwordInBytes, String clientIdentifier) {
        return  true;
    }
}
