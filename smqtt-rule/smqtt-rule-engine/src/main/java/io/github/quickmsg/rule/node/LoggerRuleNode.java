package io.github.quickmsg.rule.node;

import io.github.quickmsg.rule.RuleNode;
import lombok.Getter;
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
    public void execute(ContextView contextView) {
        executeNext(contextView);
    }


    @Override
    public void setNextRuleNode(RuleNode ruleNode) {
        this.ruleNode = ruleNode;
    }
}
