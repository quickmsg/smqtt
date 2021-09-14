package io.github.quickmsg.common;

import java.util.Map;

/**
 * @author luxurong
 */
public interface StartUp {


    /**
     * 注入环境变量
     *
     * @param environmentMap 注入不同配置
     * @see io.github.quickmsg.common.config.BootstrapConfig
     */
    default void startUp(Map<Object, Object> environmentMap) {

    }
}
