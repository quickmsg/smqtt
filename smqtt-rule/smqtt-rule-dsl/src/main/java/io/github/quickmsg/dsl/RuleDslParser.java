package io.github.quickmsg.dsl;

import io.github.quickmsg.common.rule.RuleDefinition;
import io.github.quickmsg.rule.RuleChain;

/**
 * @author luxurong
 */
public class RuleDslParser {

   private RuleChain ruleChain = RuleChain.INSTANCE;

    public RuleDslExecutor parse(RuleDefinition ruleDefinition){
        return new RuleDslExecutor(ruleChain.addRule(ruleDefinition));

    }



}
