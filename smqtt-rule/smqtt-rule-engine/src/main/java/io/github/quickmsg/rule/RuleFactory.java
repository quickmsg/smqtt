package io.github.quickmsg.rule;

import org.jeasy.rules.api.Rule;

/**
 * @author luxurong
 * @date 2021/8/21 15:03
 * @description
 */
public interface RuleFactory {

    Rule getRule(RuleType ruleType);

}
