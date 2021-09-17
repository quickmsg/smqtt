package io.github.quickmsg.dsl;

import io.github.quickmsg.common.rule.RuleChainDefinition;
import io.github.quickmsg.rule.RuleChain;

import java.util.List;

/**
 * @author luxurong
 */
public class RuleDslParser {

    private RuleChain ruleChain = RuleChain.INSTANCE;


    private final List<RuleChainDefinition> ruleChainDefinitions;

    public RuleDslParser(List<RuleChainDefinition> ruleChainDefinitions) {
        this.ruleChainDefinitions = ruleChainDefinitions;
    }

    public RuleDslExecutor parseRule() {
        if (ruleChainDefinitions != null && ruleChainDefinitions.size() > 0) {
            ruleChainDefinitions.stream().map(RuleChainDefinition::getChain).forEach(ruleChain::addRules);
        }
        return new RuleDslExecutor(ruleChain);
    }


}
