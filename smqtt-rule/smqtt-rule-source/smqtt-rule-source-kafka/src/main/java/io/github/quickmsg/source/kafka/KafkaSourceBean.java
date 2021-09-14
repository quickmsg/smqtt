package io.github.quickmsg.source.kafka;

import io.github.quickmsg.common.context.ReceiveContext;
import io.github.quickmsg.common.message.HeapMqttMessage;
import io.github.quickmsg.common.rule.RuleData;
import io.github.quickmsg.common.rule.Source;
import io.github.quickmsg.common.rule.source.SourceBean;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import reactor.util.context.ContextView;

import java.util.Map;
import java.util.Optional;
import java.util.Properties;

/**
 * kafka source bean
 *
 * @author zhaopeng
 * @date 2021/09/14
 */
public class KafkaSourceBean implements SourceBean {

    // TODO 类型
    public static KafkaProducer<String, String> producer;

    @Override
    public Boolean support(Source source) {
        return null;
    }


    /**
     * 初始化kafka
     *
     * @param sourceParam 参数
     * @return {@link Boolean}
     */
    @Override
    public Boolean bootstrap(Map<String, Object> sourceParam) {
        try {
            // 配置信息
            Properties props = new Properties();
            props.put("bootstrap.servers", sourceParam.get("bootstrapServers"));
            props.put("key.serializer", sourceParam.get("keySerializer"));
            props.put("value.serializer", sourceParam.get("valueSerializer"));

            // 创建生产者实例
            producer = new KafkaProducer<>(props);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 转发数据
     *
     * @param object 对象
     * @return {@link Object}
     */
    @Override
    public Object transmit(Object object) {
        ReceiveContext<?> receiveContext =  ((ContextView)object).get(ReceiveContext.class);
        RuleData request = ((ContextView)object).get(RuleData.class);
        HeapMqttMessage heapMqttMessage = ((ContextView)object).get(HeapMqttMessage.class);

        Optional.ofNullable(heapMqttMessage)
                .map(msg->msg.getMessage())
                .map(bytes -> new String(bytes))
                .ifPresent(message-> {
                    // 发送发布消息到kafka
                    ProducerRecord record = new ProducerRecord<String, String>("topic1", "userName", "lc");
                });



        return null;
    }

    /**
     * 关闭
     *
     * @param sourceParam 参数
     * @return {@link Boolean}
     */
    @Override
    public Boolean close(Map<String, Object> sourceParam) {
        return null;
    }
}
