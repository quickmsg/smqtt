package io.github.quickmsg.rule.node;

import io.github.quickmsg.common.channel.MockMqttChannel;
import io.github.quickmsg.common.context.ReceiveContext;
import io.github.quickmsg.common.message.HeapMqttMessage;
import io.github.quickmsg.common.protocol.ProtocolAdaptor;
import io.github.quickmsg.rule.RuleNode;
import lombok.extern.slf4j.Slf4j;
import reactor.util.context.ContextView;

/**
 * @author luxurong
 * @date 2021/8/30 11:05
 */
@Slf4j
public class TopicRuleNode implements RuleNode {

    private final String topic;

    private final String script;

    private RuleNode ruleNode;

    public TopicRuleNode(String topic, String script) {
        this.topic = topic;
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
        ReceiveContext<?> receiveContext = contextView.get(ReceiveContext.class);
        HeapMqttMessage heapMqttMessage = contextView.get(HeapMqttMessage.class);
        log.info("rule engine TopicRuleNode  request {}", heapMqttMessage);
        ProtocolAdaptor protocolAdaptor = receiveContext.getProtocolAdaptor();
        protocolAdaptor.chooseProtocol(MockMqttChannel.wrapClientIdentifier(heapMqttMessage.getClientIdentifier()), null, receiveContext);
        protocolAdaptor.chooseProtocol(MockMqttChannel.DEFAULT_MOCK_CHANNEL, null, receiveContext);
        executeNext(contextView);
    }

}
