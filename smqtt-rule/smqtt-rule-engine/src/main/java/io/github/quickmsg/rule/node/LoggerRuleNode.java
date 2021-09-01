package io.github.quickmsg.rule.node;

import io.github.quickmsg.rule.RuleNode;
import lombok.extern.slf4j.Slf4j;

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
    public Object execute(Object[] param) {
        log.info("logger rule print  {}  {}", param[0],param[1]);
        return param[0];
    }


    @Override
    public void setNextRuleNode(RuleNode ruleNode) {
        this.ruleNode = ruleNode;
    }
}
