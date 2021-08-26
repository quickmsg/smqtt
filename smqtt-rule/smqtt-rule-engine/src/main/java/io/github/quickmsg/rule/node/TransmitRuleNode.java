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

    private final RuleNode ruleNode;


    public TransmitRuleNode(Source source, RuleNode ruleNode) {
        this.source = source;
        this.ruleNode = ruleNode;
    }

    @Override
    public Object execute(Object param) {
        return SourceManager.getSourceBean(source).transmit(param);
    }

    @Override
    public RuleNode getNextRuleNode(Boolean success) {
        return this.ruleNode;
    }
}
