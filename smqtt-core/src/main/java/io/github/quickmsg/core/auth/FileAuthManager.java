package io.github.quickmsg.core.auth;

import io.github.quickmsg.common.auth.AuthBean;
import io.github.quickmsg.common.auth.AuthManager;
import io.github.quickmsg.common.config.AuthConfig;
import io.github.quickmsg.common.utils.CsvReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author luxurong
 */
public class FileAuthManager implements AuthManager {

    private final AuthConfig authConfig;

    private Map<String, AuthBean> authBeanMap = new HashMap<>();

    public FileAuthManager(AuthConfig authConfig) {
        this.authConfig = authConfig;
        List<List<String>> values = CsvReader.readCsvValues(authConfig.getFile());
        for (List<String> es : values) {
            AuthBean authBean = new AuthBean();
            authBean.setClientId(es.get(0));
            authBean.setUsername(es.get(1));
            authBean.setPassword(es.get(2));
            authBeanMap.put(authBean.getClientId(),authBean);
        }
    }

    @Override
    public Boolean auth(String userName, byte[] passwordInBytes, String clientIdentifier) {
        return Optional.ofNullable(authBeanMap.get(clientIdentifier))
                .map(authBean -> authBean.getUsername().equals(userName) && authBean.getPassword().equals(new String(passwordInBytes)))
                .orElse(false);
    }


}
