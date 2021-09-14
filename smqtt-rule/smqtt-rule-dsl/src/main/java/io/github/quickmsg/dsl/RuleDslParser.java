package io.github.quickmsg.dsl;

import io.github.quickmsg.common.rule.RuleDefinition;
import io.github.quickmsg.rule.RuleChain;

import java.util.List;

/**
 * @author luxurong
 */
public class RuleDslParser {

    private RuleChain ruleChain = RuleChain.INSTANCE;


    private final List<RuleDefinition> ruleDefinitions;

    public RuleDslParser(List<RuleDefinition> ruleDefinitions) {
        this.ruleDefinitions = ruleDefinitions;
    }

    public RuleDslExecutor parseRule() {
        if (ruleDefinitions != null && ruleDefinitions.size() > 0) {
            ruleDefinitions.forEach(ruleChain::addRule);
        }
        return new RuleDslExecutor(ruleChain);
    }


}
