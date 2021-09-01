package io.github.quickmsg.rule;

import java.util.LinkedList;

/**
 * @author luxurong
 */
public class RuleChain {

    //todo source : redis db kafka
    //todo source : redis db kafka

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
