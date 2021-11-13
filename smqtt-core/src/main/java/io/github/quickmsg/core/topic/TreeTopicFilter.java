package io.github.quickmsg.core.topic;

import io.github.quickmsg.common.channel.MqttChannel;
import io.github.quickmsg.common.metric.CounterEnum;
import io.github.quickmsg.common.metric.Metric;
import io.github.quickmsg.common.spi.DynamicLoader;
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

    private static Metric metric = DynamicLoader.findFirst(Metric.class).orElse(null);


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
            subscribeTopic.linkSubscribe();
            metric.getMetricCounter(CounterEnum.TOPIC_COUNTER).increment();
        }
    }

    @Override
    public void removeSubscribeTopic(SubscribeTopic subscribeTopic) {
        if (rootTreeNode.removeSubscribeTopic(subscribeTopic)) {
            subscribeNumber.add(-1);
            subscribeTopic.unLinkSubscribe();
            metric.getMetricCounter(CounterEnum.TOPIC_COUNTER).decrement();
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
