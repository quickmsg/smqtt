package io.github.quickmsg.rule.node;

import io.github.quickmsg.rule.RuleExecute;
import io.github.quickmsg.rule.RuleNode;
import io.github.quickmsg.rule.Source;
import io.github.quickmsg.rule.SourceManager;
import org.apache.commons.jexl3.MapContext;

import java.util.function.Consumer;

/**
 * 转发节点
 *
 * @author luxurong
 */
public class TransmitRuleNode implements RuleExecute {

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
}
