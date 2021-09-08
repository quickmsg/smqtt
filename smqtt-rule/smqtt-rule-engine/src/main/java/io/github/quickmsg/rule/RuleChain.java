package io.github.quickmsg.rule;

import io.github.quickmsg.common.rule.RuleDefinition;
import io.github.quickmsg.rule.node.EmptyNode;
import io.github.quickmsg.rule.node.LoggerRuleNode;
import io.github.quickmsg.rule.node.PredicateRuleNode;
import io.github.quickmsg.rule.node.TopicRuleNode;
import lombok.Getter;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

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
                // 待实现
            case ROCKET_MQ:
            case PREDICATE:
                return new PredicateRuleNode(definition.getScript());
            case KAFKA:
            case TOPIC:
                return new TopicRuleNode(String.valueOf(definition.getParam()));
            case LOG:
                return new LoggerRuleNode();
            default:
                return new EmptyNode();
        }
    }


    public Disposable executeRule(Object object) {
        return Mono.deferContextual(contextView -> Mono.fromRunnable(() -> {
            ruleNodeList.forEach(ruleNode -> ruleNode.execute(contextView));
        })).contextWrite(context -> context.put("msg", object)).subscribeOn(Schedulers.parallel()).subscribe();
    }

}
