package io.github.quickmsg.rule.node;

import io.github.quickmsg.common.message.HeapMqttMessage;
import io.github.quickmsg.common.rule.Source;
import io.github.quickmsg.rule.RuleNode;
import io.github.quickmsg.source.SourceManager;
import reactor.util.context.ContextView;

import java.util.Optional;

/**
 * 转发节点
 *
 * @author luxurong
 */
public class TransmitRuleNode implements RuleNode {

    private final Source source;

    private final String script;

    private RuleNode ruleNode;


    public TransmitRuleNode(Source source, String script) {
        this.source = source;
        this.script = script;
    }

    @Override
    public void execute(ContextView contextView) {
        HeapMqttMessage heapMqttMessage = contextView.get(HeapMqttMessage.class);
        Object param = Optional.ofNullable(script)
                .map(spt ->
                        triggerTemplate(spt, context -> {
                            HeapMqttMessage mqttMessage = contextView.get(HeapMqttMessage.class);
                            mqttMessage.getKeyMap().forEach(context::set);
                        }))
                .orElseGet(() -> new String(heapMqttMessage.getMessage()));
        SourceManager.getSourceBean(source).transmit(param);
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
