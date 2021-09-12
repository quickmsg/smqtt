package io.github.quickmsg.common.rule;

/**
 * @author luxurong
 */
public interface DslExecutor {

    /**
     * 执行
     * @param object 请求参数
=     */
    void executeRule(Object... object);

}
