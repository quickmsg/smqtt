package io.github.quickmsg.rule.node;

import io.github.quickmsg.rule.RuleNode;
import lombok.extern.slf4j.Slf4j;
import reactor.util.context.ContextView;

/**
 * @author luxurong
 */
@Slf4j
public class LoggerRuleNode implements RuleNode {

    private RuleNode ruleNode;


    @Override
    public RuleNode getNextRuleNode() {
        return this.ruleNode;
    }

    @Override
    public Boolean execute(ContextView contextView) {
        return true;
    }


    @Override
    public void setNextRuleNode(RuleNode ruleNode) {
        this.ruleNode = ruleNode;
    }
}
