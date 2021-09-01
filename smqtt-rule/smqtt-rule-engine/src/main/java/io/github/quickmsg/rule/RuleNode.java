package io.github.quickmsg.rule;

/**
 * @author luxurong
 */
public interface RuleNode extends RuleExecute {

    /***
     * 获取下一个RuleNode
     * @return {@link RuleNode}
     */
    RuleNode getNextRuleNode();


    /***
     * 设置下一个RuleNode
     * @param ruleNode {@link RuleNode}
     */
    void setNextRuleNode(RuleNode ruleNode);

}
