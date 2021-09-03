package io.github.quickmsg.rule;

import lombok.Data;
import lombok.Getter;

import java.util.LinkedList;

/**
 * @author luxurong
 */
@Getter
public class RuleChain {

    private static volatile RuleChain ruleChain;

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

    public static RuleChain getSingleton() {
        if (ruleChain == null) {
            synchronized (RuleChain.class) {
                if (ruleChain == null) {
                    ruleChain = new RuleChain();
                }
            }
        }
        return ruleChain;
    }


}
