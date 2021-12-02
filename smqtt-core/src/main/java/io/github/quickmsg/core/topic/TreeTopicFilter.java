package io.github.quickmsg.core.topic;

import io.github.quickmsg.common.channel.MqttChannel;
import io.github.quickmsg.common.metric.CounterType;
import io.github.quickmsg.common.metric.MetricManagerHolder;
import io.github.quickmsg.common.topic.SubscribeTopic;
import io.netty.handler.codec.mqtt.MqttQoS;

import java.util.Set;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

/**
 * @author luxurong
 */
public class TreeTopicFilter implements TopicFilter {

    private TreeNode rootTreeNode = new TreeNode("root");

    private LongAdder subscribeNumber = new LongAdder();


    @Override
    public Set<SubscribeTopic> getSubscribeByTopic(String topic, MqttQoS mqttQoS) {
        return rootTreeNode.getSubscribeByTopic(topic).stream().map(tp -> tp.compareQos(mqttQoS)).collect(Collectors.toSet());
    }

    @Override
    public void addSubscribeTopic(String topicFilter, MqttChannel mqttChannel, MqttQoS mqttQoS) {
        this.addSubscribeTopic(new SubscribeTopic(topicFilter, mqttQoS, mqttChannel));
    }

    @Override
    public void addSubscribeTopic(SubscribeTopic subscribeTopic) {
        if (rootTreeNode.addSubscribeTopic(subscribeTopic)) {
            subscribeNumber.add(1);
            MetricManagerHolder.metricManager.getMetricRegistry().getMetricCounter(CounterType.SUBSCRIBE).increment();
            subscribeTopic.linkSubscribe();
        }
    }

    @Override
    public void removeSubscribeTopic(SubscribeTopic subscribeTopic) {
        if (rootTreeNode.removeSubscribeTopic(subscribeTopic)) {
            subscribeNumber.add(-1);
            MetricManagerHolder.metricManager.getMetricRegistry().getMetricCounter(CounterType.SUBSCRIBE).decrement();
            subscribeTopic.unLinkSubscribe();
        }
    }

    @Override
    public int count() {
        return (int) subscribeNumber.sum();
    }

    @Override
    public Set<SubscribeTopic> getAllSubscribesTopic() {
        return rootTreeNode.getAllSubscribesTopic();
    }


}
