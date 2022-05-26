package io.github.quickmsg.common.auth;

/**
 * @author luxurong
 */
public interface AuthManager {

    /**
     * 认证接口
     *
     * @param userName        用户名称
     * @param passwordInBytes 密钥
     * @param clientIdentifier 设备标志
     * @return 布尔
     */
    Boolean auth(String userName, byte[] passwordInBytes, String clientIdentifier);

}
