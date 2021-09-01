package io.github.quickmsg.rule;

import java.util.LinkedList;

/**
 * @author luxurong
 */
public class RuleChain {

    private LinkedList<RuleNode> ruleNodeList = new LinkedList<>();

    public void addRule(RuleDefinition definition) {
        RuleDefinition root = definition;
        RuleNode rootNode = definition.parseNode();
        RuleNode preNode = rootNode;
        while (root != null) {
            RuleNode node = root.parseNode();
            preNode.setNextRuleNode(node);
            preNode = node;
            root = root.getNextDefinition();
        }
        ruleNodeList.addLast(rootNode);
    }

}
