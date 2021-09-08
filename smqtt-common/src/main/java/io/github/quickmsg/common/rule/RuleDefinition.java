package io.github.quickmsg.common.rule;

import io.github.quickmsg.common.enums.RuleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author luxurong
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RuleDefinition {


    private String ruleName;

    private RuleType ruleType;

    private Object param;

    private String script;

    private RuleDefinition nextDefinition;


}