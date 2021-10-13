package io.github.quickmsg.rule;

import io.github.quickmsg.common.rule.RuleDefinition;
import io.github.quickmsg.common.rule.source.Source;
import io.github.quickmsg.rule.node.*;
import lombok.Getter;
import reactor.core.publisher.Mono;
import reactor.util.context.ContextView;

import java.util.LinkedList;
import java.util.List;

/**
 * @author luxurong
 */
@Getter
public class RuleChain {

    private RuleChain() {
    }

    public final static RuleChain INSTANCE = new RuleChain();

    private LinkedList<RuleNode> ruleNodeList = new LinkedList<>();

    public void addRules(List<RuleDefinition> definitions) {
        RuleNode rootNode = this.parseNode(definitions.get(0));
        RuleNode preNode = rootNode;
        for (int i = 1; i < definitions.size(); i++) {
            RuleNode node = this.parseNode(definitions.get(i));
            preNode.setNextRuleNode(node);
            preNode = node;
        }
        ruleNodeList.addLast(rootNode);
    }


    private RuleNode parseNode(RuleDefinition definition) {
        switch (definition.getRuleType()) {
            case HTTP:
                return new TransmitRuleNode(Source.HTTP, definition.getScript());
            case PREDICATE:
                return new PredicateRuleNode(definition.getScript());
            case KAFKA:
                return new TransmitRuleNode(Source.KAFKA, definition.getScript());
            case TOPIC:
                return new TopicRuleNode(definition.getScript());
            case LOG:
                return new LoggerRuleNode(definition.getScript());
            case ROCKET_MQ:
                return new TransmitRuleNode(Source.ROCKET_MQ, definition.getScript());
            case RABBIT_MQ:
                return new TransmitRuleNode(Source.RABBIT_MQ, definition.getScript());
            case DATA_BASE:
                return new DatabaseRuleNode(definition.getScript());
            case MQTT:
                return new TransmitRuleNode(Source.MQTT, definition.getScript());
            default:
                return new EmptyNode();
        }
    }


    public Mono<Void> executeRule(ContextView contextView) {
        return Mono.fromRunnable(() -> ruleNodeList.forEach(ruleNode -> ruleNode.execute(contextView)));
    }

}
