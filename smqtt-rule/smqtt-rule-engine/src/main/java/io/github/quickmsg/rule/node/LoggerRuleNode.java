package io.github.quickmsg.rule.node;

import io.github.quickmsg.common.message.HeapMqttMessage;
import io.github.quickmsg.common.utils.JacksonUtil;
import io.github.quickmsg.rule.RuleNode;
import lombok.extern.slf4j.Slf4j;
import reactor.util.context.ContextView;

import java.util.Optional;

/**
 * @author luxurong
 */
@Slf4j
public class LoggerRuleNode implements RuleNode {

    private final static String DEFAULT_LOG_TEMPLATE = "logger rule accept msg : %s";

    private RuleNode ruleNode;

    private final String script;

    public LoggerRuleNode(String script) {
        this.script = script;
    }


    @Override
    public RuleNode getNextRuleNode() {
        return this.ruleNode;
    }

    @Override
    public void execute(ContextView contextView) {
        HeapMqttMessage heapMqttMessage = contextView.get(HeapMqttMessage.class);
        String logInfo = Optional.ofNullable(script)
                .map(sc ->
                        String.valueOf(triggerScript(script, context -> heapMqttMessage.getKeyMap().forEach(context::set)))
                ).orElseGet(() -> String.format(DEFAULT_LOG_TEMPLATE, JacksonUtil.map2Json(heapMqttMessage.getKeyMap())));
        log.info(logInfo);
        executeNext(contextView);
    }


    @Override
    public void setNextRuleNode(RuleNode ruleNode) {
        this.ruleNode = ruleNode;
    }
}
