package io.github.quickmsg.rule;

import io.github.quickmsg.common.rule.RuleDefinition;
import io.github.quickmsg.rule.node.*;
import io.github.quickmsg.source.Source;
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

    public RuleChain addRule(RuleDefinition definition) {
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
        return this;
    }


    private RuleNode parseNode(RuleDefinition definition) {
        switch (definition.getRuleType()) {
            case HTTP:
                return new TransmitRuleNode(Source.HTTP);
            case PREDICATE:
                return new PredicateRuleNode(definition.getScript());
            case KAFKA:
                return new TransmitRuleNode(Source.KAFKA);
            case TOPIC:
                return new TopicRuleNode(String.valueOf(definition.getParam()));
            case LOG:
                return new LoggerRuleNode();
            case ROCKET_MQ:
                return new TransmitRuleNode(Source.ROCKET_MQ);
            case H_BASE:
                return new TransmitRuleNode(Source.H_BASE);
            default:
                return new EmptyNode();
        }
    }


    public Mono<Void> executeRule(ContextView contextView) {
        return Mono.fromRunnable(() -> ruleNodeList.forEach(ruleNode -> ruleNode.execute(contextView)));
    }

}
