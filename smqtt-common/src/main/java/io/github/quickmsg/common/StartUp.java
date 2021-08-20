package io.github.quickmsg.common;

import io.github.quickmsg.common.config.BootstrapConfig;

/**
 * @author luxurong
 */
public interface StartUp {


    /**
     * 注入环境变量
     *
     * @param bootstrapConfig 环境变量
     */
    default void startUp(BootstrapConfig bootstrapConfig) {

    }
}
