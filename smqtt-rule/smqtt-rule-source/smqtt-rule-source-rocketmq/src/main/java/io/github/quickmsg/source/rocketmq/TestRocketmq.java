package io.github.quickmsg.source.rocketmq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

public class TestRocketmq {
    public static void main(String[] args) throws MQClientException {
        // TODO 1 创建消费者，指定所属的消费者组名
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer("testProducerGroup");
        // TODO 2 指定NameServer的地址
        defaultMQPushConsumer.setNamesrvAddr("172.16.70.156:9876");
        // TODO 3 指定消费者订阅的主题和标签
        defaultMQPushConsumer.subscribe("testTopic", "*");
        // TODO 4 进行订阅：注册回调函数，编写处理消息的逻辑
        defaultMQPushConsumer.registerMessageListener((List<MessageExt> list, ConsumeConcurrentlyContext context) -> {
            try {
                String s = new String(list.get(0).getBody());
                System.out.println(s);
                System.out.println("收到消息--》" + list);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });

        // TODO 5 启动消费者
        defaultMQPushConsumer.start();
        System.out.println("消费者启动成功。。。");
    }
}
