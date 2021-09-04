package io.github.quickmsg.rule;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.quickmsg.common.enums.RuleType;
import io.github.quickmsg.rule.node.LoggerRuleNode;
import io.github.quickmsg.rule.node.TopicRuleNode;
import io.github.quickmsg.rule.node.TransmitRuleNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author luxurong
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleDefinition {

    private RuleType ruleType;

    private Object param;

    private RuleDefinition nextDefinition;


    public RuleNode parseNode(){
        switch (ruleType){
            case LOG:
                return new LoggerRuleNode();
            case TOPIC:
                return new TopicRuleNode(param.toString());
            case FILTER:
            case KAFKA:
                //todo 待实现
            case ROCKET_MQ:
                //todo 待实现
            default:
                return new TransmitRuleNode(null);
        }
    }

}
