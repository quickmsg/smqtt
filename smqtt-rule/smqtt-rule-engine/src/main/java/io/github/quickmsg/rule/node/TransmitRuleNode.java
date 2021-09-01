package io.github.quickmsg.rule.node;

import io.github.quickmsg.rule.RuleNode;
import io.github.quickmsg.source.Source;
import io.github.quickmsg.source.SourceManager;

/**
 * 转发节点
 *
 * @author luxurong
 */
public class TransmitRuleNode implements RuleNode {

    private final Source source;

    private  RuleNode ruleNode;


    public TransmitRuleNode(Source source) {
        this.source = source;
    }

    @Override
    public Object execute(Object[] param) {
        return SourceManager.getSourceBean(source).transmit(param);
    }

    @Override
    public RuleNode getNextRuleNode() {
        return this.ruleNode;
    }


    @Override
    public void setNextRuleNode(RuleNode ruleNode) {
        this.ruleNode = ruleNode;
    }

}
