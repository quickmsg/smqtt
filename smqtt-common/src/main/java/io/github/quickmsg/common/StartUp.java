package io.github.quickmsg.common;

import io.github.quickmsg.common.environment.EnvContext;

/**
 * @author luxurong
 */
public interface StartUp {


    /**
     * 注入环境变量
     *
     * @param envContext 环境变量
     */
    default void startUp(EnvContext envContext) {

    }
}
