package io.github.quickmsg.common.rule;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author luxurong
 * @date 2021/9/15 20:29
 */
@Getter
@Setter
@ToString
public class RuleChainDefinition {

    private String ruleName;

    private List<RuleDefinition> chain;

}
