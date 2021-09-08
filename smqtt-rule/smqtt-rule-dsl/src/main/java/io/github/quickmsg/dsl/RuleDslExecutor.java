package io.github.quickmsg.dsl;

import io.github.quickmsg.rule.RuleChain;

/**
 * @author luxurong
 */

public class RuleDslExecutor {

    private final RuleChain ruleChain;

    public RuleDslExecutor(RuleChain ruleChain) {
        this.ruleChain = ruleChain;
    }

    public void executeRule(Object object) {
        ruleChain.executeRule(object);
    }
}
