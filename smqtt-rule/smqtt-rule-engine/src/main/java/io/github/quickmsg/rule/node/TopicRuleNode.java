package io.github.quickmsg.rule.node;

import io.github.quickmsg.common.channel.MockMqttChannel;
import io.github.quickmsg.common.config.Configuration;
import io.github.quickmsg.common.context.ReceiveContext;
import io.github.quickmsg.common.message.SmqttMessage;
import io.github.quickmsg.common.protocol.ProtocolAdaptor;
import io.github.quickmsg.rule.RuleNode;
import io.netty.handler.codec.mqtt.MqttMessage;

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
    public RuleNode getNextRuleNode(Boolean success) {
        return this.ruleNode;
    }

    @Override
    public Object execute(Object[] param) {
        ReceiveContext<?> receiveContexts=(ReceiveContext)param[2];
        Object msg=param[0];
        ProtocolAdaptor protocolAdaptor=receiveContexts.getProtocolAdaptor();
        protocolAdaptor.chooseProtocol(MockMqttChannel.DEFAULT_MOCK_CHANNEL, (SmqttMessage<MqttMessage>) msg,receiveContexts);
        return msg;
    }

}
