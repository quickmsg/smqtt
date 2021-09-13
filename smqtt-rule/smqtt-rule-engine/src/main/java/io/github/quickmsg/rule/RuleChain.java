package io.github.quickmsg.rule;

import io.github.quickmsg.common.rule.RuleDefinition;
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
            case WEB_HOOK:
                return new WebHookNode();
            case PREDICATE:
                return new PredicateRuleNode(definition.getScript());
            case KAFKA:
            case TOPIC:
                return new TopicRuleNode(String.valueOf(definition.getParam()));
            case LOG:
                return new LoggerRuleNode();
            case ROCKET_MQ: //todo 待实现
            default:
                return new EmptyNode();
        }
    }


    public Mono<Void> executeRule(ContextView contextView) {
        return Mono.fromRunnable(() -> ruleNodeList.forEach(ruleNode -> ruleNode.execute(contextView)));
    }

}
