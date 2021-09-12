package io.github.quickmsg.rule.node;

import io.github.quickmsg.common.channel.MockMqttChannel;
import io.github.quickmsg.common.context.ReceiveContext;
import io.github.quickmsg.common.message.SmqttMessage;
import io.github.quickmsg.common.protocol.ProtocolAdaptor;
import io.github.quickmsg.rule.RuleNode;
import io.netty.handler.codec.mqtt.MqttMessage;
import reactor.util.context.ContextView;

/**
 * @author luxurong
 * @date 2021/8/30 11:05
 * @description
 */
public class TopicRuleNode implements RuleNode {

    private final String topic;

    private  RuleNode ruleNode;

    public TopicRuleNode(String topic){
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
        ReceiveContext<?> receiveContext=contextView.get(ReceiveContext.class);
        ProtocolAdaptor protocolAdaptor = receiveContext.getProtocolAdaptor();
        protocolAdaptor.chooseProtocol(MockMqttChannel.DEFAULT_MOCK_CHANNEL, new SmqttMessage<>(),receiveContext);
        executeNext(contextView);
    }

}
