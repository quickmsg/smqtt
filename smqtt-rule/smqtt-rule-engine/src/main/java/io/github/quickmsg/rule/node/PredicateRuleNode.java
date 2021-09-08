package io.github.quickmsg.rule.node;

import io.github.quickmsg.rule.RuleNode;
import reactor.util.context.ContextView;

/**
 * @author luxurong
 * @date 2021/8/23 19:34
 * @description
 */
public class PredicateRuleNode implements RuleNode {

    private final String script;

    private RuleNode ruleNode;


    public PredicateRuleNode(String script) {
        this.script = script;
    }

    @Override
    public RuleNode getNextRuleNode() {
        return this.ruleNode;
    }


    @Override
    public void setNextRuleNode(RuleNode ruleNode) {
        this.ruleNode = ruleNode;
    }

    @Override
    public Boolean execute(ContextView contextView) {
        return (Boolean) triggerScript(script, new Object(), context -> {
        });
    }
}
