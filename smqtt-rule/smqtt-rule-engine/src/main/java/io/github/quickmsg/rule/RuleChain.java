package io.github.quickmsg.rule;

import io.github.quickmsg.common.rule.RuleDefinition;
import io.github.quickmsg.common.rule.source.Source;
import io.github.quickmsg.rule.node.*;
import lombok.Getter;
import reactor.core.publisher.Mono;
import reactor.util.context.ContextView;

import java.util.LinkedList;

/**
 * @author luxurong
 */
@Getter
public class RuleChain {

    private RuleChain() {
    }

    public final static RuleChain INSTANCE = new RuleChain();

    private LinkedList<RuleNode> ruleNodeList = new LinkedList<>();

    public void addRule(RuleDefinition definition) {
        RuleDefinition root = definition;
        RuleNode rootNode = this.parseNode(definition);
        RuleNode preNode = rootNode;
        while (root != null) {
            RuleNode node = this.parseNode(definition);
            preNode.setNextRuleNode(node);
            preNode = node;
            root = root.getNextDefinition();
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
                return new TopicRuleNode(String.valueOf(definition.getParam()),definition.getScript());
            case LOG:
                return new LoggerRuleNode();
            case ROCKET_MQ:
                return new TransmitRuleNode(Source.ROCKET_MQ, definition.getScript());
            case H_BASE:
                return new TransmitRuleNode(Source.H_BASE, definition.getScript());
            default:
                return new EmptyNode();
        }
    }


    public Mono<Void> executeRule(ContextView contextView) {
        return Mono.fromRunnable(() -> ruleNodeList.forEach(ruleNode -> ruleNode.execute(contextView)));
    }

}
