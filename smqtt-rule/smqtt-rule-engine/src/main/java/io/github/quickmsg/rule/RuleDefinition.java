package io.github.quickmsg.rule;

import io.github.quickmsg.rule.node.LoggerRuleNode;
import io.github.quickmsg.rule.node.TopicRuleNode;
import lombok.Data;

/**
 * @author luxurong
 */

@Data
public class RuleDefinition {

    private RuleType ruleType;

    private Object param;


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
                return null;
        }
    }

}
