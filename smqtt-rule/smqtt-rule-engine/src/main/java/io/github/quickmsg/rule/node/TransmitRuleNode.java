package io.github.quickmsg.rule.node;

import io.github.quickmsg.rule.RuleNode;
import io.github.quickmsg.source.Source;
import reactor.util.context.ContextView;

/**
 * 转发节点
 *
 * @author luxurong
 */
public class TransmitRuleNode implements RuleNode {

    private final Source source;

    private RuleNode ruleNode;


    public TransmitRuleNode(Source source) {
        this.source = source;
    }

    @Override
    public Boolean execute(ContextView contextView) {
//        return SourceManager.getSourceBean(source).transmit(param);
        return false;
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
