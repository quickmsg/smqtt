package io.github.quickmsg.rule;

/**
 * @author luxurong
 */
public interface RuleNode extends RuleExecute {

    /***
     * 获取下一个RuleNode
     * @param  success 前置节点filter
     * @return {@link RuleNode}
     */
    RuleNode getNextRuleNode(Boolean success);

}
