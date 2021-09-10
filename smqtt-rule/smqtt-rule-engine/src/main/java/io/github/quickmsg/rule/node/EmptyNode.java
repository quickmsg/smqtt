package io.github.quickmsg.rule.node;

import io.github.quickmsg.rule.RuleNode;
import reactor.util.context.ContextView;

/**
 * @author luxurong
 */
public class EmptyNode implements RuleNode {


    @Override
    public RuleNode getNextRuleNode() {
        return null;
    }

    @Override
    public void setNextRuleNode(RuleNode ruleNode) {

    }

    @Override
    public void execute(ContextView contextView) {
    }
}
