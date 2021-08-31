package io.github.quickmsg.rule;

import java.util.LinkedList;
import java.util.List;

/**
 * @author luxurong
 */
public class RuleChain {

    private LinkedList<RuleNode> ruleNodeList = new LinkedList<>();

    public void addRule(RuleDefinition definition) {
        ruleNodeList.addLast(definition.parseNode());
    }

}
