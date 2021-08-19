package io.github.quickmsg.rule;

import lombok.Data;

/**
 * @author luxurong
 */
@Data
public class MqttRule {

    private RuleType ruleType;

    private String expression;

    private RuleAction ruleAction;



}
