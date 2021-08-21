package io.github.quickmsg.rule;

/**
 * @author luxurong
 */
public interface RuleManager {


    /**
     * 注册规则
     *
     * @param rule #{@link EventRule}
     */
    void registry(EventRule rule);

    /**
     * 删除规则引擎
     *
     * @param rule #{@link EventRule}
     */
    void unRegistry(EventRule rule);

    /**
     * 开始规则
     *
     * @param rule #{@link EventRule}
     */
    void pipeline(EventRule rule);


}
