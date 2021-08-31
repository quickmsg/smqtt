package io.github.quickmsg.rule.node;

import io.github.quickmsg.rule.RuleNode;

/**
 * @author luxurong
 * @date 2021/8/23 19:34
 * @description
 */
public class  PredicateRuleNode implements RuleNode {

    private final String script;

    private final RuleNode trueRuleNode;

    private final RuleNode falseRuleNode;


    public PredicateRuleNode(String script, RuleNode trueRuleNode, RuleNode falseRuleNode) {
        this.script = script;
        this.trueRuleNode = trueRuleNode;
        this.falseRuleNode = falseRuleNode;
    }

    @Override
    public RuleNode getNextRuleNode(Boolean success) {
        return success ? trueRuleNode : falseRuleNode;
    }

    @Override
    public Object execute(Object[] param) {
        return triggerScript(script, param, context -> {});
    }
}
