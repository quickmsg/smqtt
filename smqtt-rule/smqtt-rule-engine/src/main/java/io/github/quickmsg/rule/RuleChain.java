package io.github.quickmsg.rule;

import io.github.quickmsg.common.rule.RuleDefinition;
import lombok.Getter;

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
        return null;
    }

}
