package io.github.quickmsg.rule;

import lombok.Data;

import java.util.List;

/**
 * @author luxurong
 */
@Data
public class MqttRule {

    private RuleType ruleType;

    private String condition;

    private List<RuleAction> ruleAction;


}
