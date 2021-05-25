package io.github.quickmsg.common;

import io.github.quickmsg.common.environment.EnvContext;

/**
 * @author luxurong
 * @date 2021/5/25 16:48
 * @description
 */
public interface StartUp {


    /**
     * 注入环境变量
     *
     * @param envContext 环境变量
     * @return 布尔
     */
    default void startUp(EnvContext envContext) {

    }
}
