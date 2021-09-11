package io.github.quickmsg.rule.node;

import io.github.quickmsg.rule.RuleNode;
import io.github.quickmsg.source.Source;
import io.github.quickmsg.source.SourceManager;
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
    public void execute(ContextView contextView) {
        SourceManager.getSourceBean(source).transmit(contextView);
        executeNext(contextView);
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
