package io.github.quickmsg.rule.node;

import io.github.quickmsg.common.channel.MockMqttChannel;
import io.github.quickmsg.common.context.ReceiveContext;
import io.github.quickmsg.common.message.HeapMqttMessage;
import io.github.quickmsg.common.message.MqttMessageBuilder;
import io.github.quickmsg.common.message.SmqttMessage;
import io.github.quickmsg.common.protocol.ProtocolAdaptor;
import io.github.quickmsg.rule.RuleNode;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.extern.slf4j.Slf4j;
import reactor.util.context.ContextView;

import java.util.Objects;

/**
 * @author luxurong
 */
@Slf4j
public class TopicRuleNode implements RuleNode {

    private final String topic;

    private RuleNode ruleNode;

    public TopicRuleNode(String topic) {
        Objects.requireNonNull(topic);
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
        HeapMqttMessage heapMqttMessage = contextView.get(HeapMqttMessage.class);
        log.info("rule engine TopicRuleNode  request {}", heapMqttMessage);
        ProtocolAdaptor protocolAdaptor = receiveContext.getProtocolAdaptor();
        protocolAdaptor.chooseProtocol(MockMqttChannel.wrapClientIdentifier(heapMqttMessage.getClientIdentifier()),
                new SmqttMessage<>(getMqttMessage(heapMqttMessage),heapMqttMessage.getTimestamp(),Boolean.TRUE), receiveContext);
        executeNext(contextView);
    }


    private MqttPublishMessage getMqttMessage(HeapMqttMessage heapMqttMessage) {
        return MqttMessageBuilder
                .buildPub(false,
                        MqttQoS.valueOf(heapMqttMessage.getQos()),
                        0,
                        this.topic,
                        PooledByteBufAllocator.DEFAULT.buffer().writeBytes(heapMqttMessage.getMessage()),
                        heapMqttMessage.getProperties());
    }


}
