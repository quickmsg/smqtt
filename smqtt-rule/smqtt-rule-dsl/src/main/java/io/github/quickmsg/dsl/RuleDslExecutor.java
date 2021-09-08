package io.github.quickmsg.dsl;

import io.github.quickmsg.rule.RuleChain;
import io.github.quickmsg.rule.RuleExecute;

/**
 * @author luxurong
 */

public class RuleDslExecutor implements RuleExecute {

    private final RuleChain ruleChain;

    public RuleDslExecutor(RuleChain ruleChain) {
        this.ruleChain = ruleChain;
    }

    @Override
    public Object execute(Object[] param) {
        return ruleChain.getRuleNodeList().;
    }
}
