package io.github.quickmsg.common.rule;

import io.github.quickmsg.common.enums.RuleType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author luxurong
 */
@Getter
@Setter
@ToString
public class RuleDefinition {


    private String ruleName;

    private RuleType ruleType;

    private String script;


}