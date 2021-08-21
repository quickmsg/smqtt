package io.github.quickmsg.rule;

/**
 * @author luxurong
 */
public interface RuleAction {

    /**
     * 执行
     *
     * @param param 参数
     */
    void execute(Object param);

}
