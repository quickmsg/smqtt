package io.github.quickmsg.rule.node;

import io.github.quickmsg.common.message.HeapMqttMessage;
import io.github.quickmsg.common.rule.source.Source;
import io.github.quickmsg.rule.RuleNode;
import io.github.quickmsg.rule.source.SourceManager;
import reactor.util.context.ContextView;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author luxurong
 */
public class DatabaseRuleNode implements RuleNode {

    private final String script;

    private RuleNode ruleNode;

    public DatabaseRuleNode(String script) {
        this.script = script;
    }

    @Override
    public RuleNode getNextRuleNode() {
        return this.ruleNode;
    }


    @Override
    public void setNextRuleNode(RuleNode ruleNode) {
        this.ruleNode = ruleNode;
    }

    @Override
    public void execute(ContextView contextView) {
        HeapMqttMessage heapMqttMessage = contextView.get(HeapMqttMessage.class);
        Map<String, Object> param = new HashMap<>();
        if (script != null) {
            Object obj = triggerTemplate(script, context -> heapMqttMessage.getKeyMap().forEach(context::set));
            param.put("sql", String.valueOf(obj));
            Optional.ofNullable(SourceManager.getSourceBean(Source.DATA_BASE))
                    .ifPresent(sourceBean -> sourceBean.transmit(param));
        }
        executeNext(contextView);
    }
}



