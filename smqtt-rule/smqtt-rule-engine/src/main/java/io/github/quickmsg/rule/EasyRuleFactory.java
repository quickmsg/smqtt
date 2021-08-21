package io.github.quickmsg.rule;

import org.jeasy.rules.api.Rule;
import org.jeasy.rules.core.BasicRule;
import org.jeasy.rules.core.RuleBuilder;
import org.jeasy.rules.spel.SpELRule;

/**
 * @author luxurong
 * @date 2021/8/21 15:03
 * @description
 */
public class EasyRuleFactory implements RuleFactory {


    @Override
    public Rule getRule(RuleType ruleType) {
        switch (ruleType){
            case JEXL:
            case MVEL:
            case SPEL:
                return new SpELRule();
            default:
                return new RuleBuilder().build()
        }
    }
}
