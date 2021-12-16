package topic;

import io.github.quickmsg.common.channel.MqttChannel;
import io.github.quickmsg.common.topic.SubscribeTopic;
import io.github.quickmsg.common.topic.TopicRegistry;
import io.github.quickmsg.core.spi.DefaultTopicRegistry;
import io.netty.handler.codec.mqtt.MqttQoS;

import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.*;

/**
 * @author luxurong
 */
public class TopicTest {

   static ExecutorService service = Executors.newFixedThreadPool(100);

    private static TopicRegistry topicRegistry = new DefaultTopicRegistry();

    private static Map<Integer, MqttChannel> channelMap  = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        CountDownLatch count = new CountDownLatch(500000);
        for(int i=0;i<500000;i++){
            service.execute(()->{
                int index = new Random().nextInt(1000);
                MqttChannel mqttChannel =channelMap.computeIfAbsent(index,in->{
                    MqttChannel mqttChannel1=new MqttChannel();
                    mqttChannel1.setTopics(new CopyOnWriteArraySet<>());
                    return mqttChannel1;
                });
                SubscribeTopic subscribeTopic=new SubscribeTopic(String.valueOf(index), MqttQoS.AT_MOST_ONCE,mqttChannel);
                topicRegistry.registrySubscribeTopic(subscribeTopic);
                topicRegistry.getAllTopics();
                count.countDown();
            });
        }
        try {
            count.await();
            Map<String, Set<MqttChannel>> topics =  topicRegistry.getAllTopics();
            System.out.println(topics);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}
