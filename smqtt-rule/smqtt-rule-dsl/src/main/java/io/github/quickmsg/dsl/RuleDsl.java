package io.github.quickmsg.dsl;

import io.github.quickmsg.common.enums.RuleType;
import lombok.Data; 

import java.util.List;

/**
 * @author luxurong
 */
@Data
public class RuleDsl {

    private String name;

    private List<String> rules;

    private RuleType ruleType;

    private String action;

}
