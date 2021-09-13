package io.github.quickmsg.rule.node;

import io.github.quickmsg.common.channel.MockMqttChannel;
import io.github.quickmsg.common.context.ReceiveContext;
import io.github.quickmsg.common.protocol.ProtocolAdaptor;
import io.github.quickmsg.common.rule.RuleData;
import io.github.quickmsg.rule.RuleNode;
import io.netty.handler.codec.mqtt.MqttMessageType;
import lombok.extern.slf4j.Slf4j;
import reactor.util.context.ContextView;

/**
 * @author luxurong
 * @date 2021/8/30 11:05
 */
@Slf4j
public class TopicRuleNode implements RuleNode {

    private final String topic;

    private RuleNode ruleNode;

    public TopicRuleNode(String topic) {
        this.topic = topic;
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
        RuleData request = contextView.get(RuleData.class);
        // topic 只转发publish消息
        if (request.getMsgType() == MqttMessageType.PUBLISH) {
            ProtocolAdaptor protocolAdaptor = receiveContext.getProtocolAdaptor();
            // 生成消息
            protocolAdaptor.chooseProtocol(MockMqttChannel.DEFAULT_MOCK_CHANNEL, null, receiveContext);
        } else {
            log.warn("TopicRuleNode discard request {}", request);
        }
        executeNext(contextView);
    }

}
