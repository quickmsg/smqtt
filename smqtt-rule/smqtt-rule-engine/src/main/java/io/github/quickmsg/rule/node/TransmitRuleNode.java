package io.github.quickmsg.rule.node;

import io.github.quickmsg.common.message.HeapMqttMessage;
import io.github.quickmsg.common.rule.source.Source;
import io.github.quickmsg.common.rule.source.SourceBean;
import io.github.quickmsg.common.utils.JacksonUtil;
import io.github.quickmsg.rule.RuleNode;
import io.github.quickmsg.rule.source.SourceManager;
import lombok.extern.slf4j.Slf4j;
import reactor.util.context.ContextView;

import java.util.Map;

/**
 * 转发节点
 *
 * @author luxurong
 */
@Slf4j
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
        Map<String, Object> param;
        if (script != null) {
            Object obj = triggerTemplate(script, context -> heapMqttMessage.getKeyMap().forEach(context::set));
            param = JacksonUtil.json2Map(obj.toString(), String.class, Object.class);

        } else {
            param = heapMqttMessage.getKeyMap();
        }
        SourceBean sourceBean = SourceManager.getSourceBean(source);
        if(sourceBean == null){
            log.warn("[ please set source {}]",source);
        }
        else{
            sourceBean.transmit(param);
        }
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
