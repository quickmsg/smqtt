package io.github.quickmsg.source.mqtt;

import io.github.quickmsg.common.rule.source.Source;
import io.github.quickmsg.common.rule.source.SourceBean;
import io.github.quickmsg.common.utils.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Map;
import java.util.Optional;
import java.util.Properties;

/**
 * kafka source bean
 *
 * @author zhaopeng
 */
@Slf4j
public class KafkaSourceBean implements SourceBean {

    private KafkaProducer<String, Object> producer;

    private String topic;

    @Override
    public Boolean support(Source source) {
        return source == Source.KAFKA;
    }


    /**
     * 初始化kafka
     *
     * @param sourceParam 参数
     * @return Boolean
     */
    @Override
    public Boolean bootstrap(Map<String, Object> sourceParam) {
        try {
            // 配置信息
            Properties props = new Properties();
            for (String key : sourceParam.keySet()) {
                props.put(key.replaceAll("-", "."), sourceParam.get(key));
            }
            topic = Optional.ofNullable(sourceParam.get("topic")).map(String::valueOf).orElse("smqtt");
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
     */
    @Override
    public void transmit(Map<String, Object> object) {
        String json = JacksonUtil.bean2Json(object);
        log.info("kafka send msg {}", json);
        if (producer != null) {
            ProducerRecord<String, Object> record = new ProducerRecord<>(topic, json);
            producer.send(record);
        }
    }


    @Override
    public void close() {
        if (producer != null) {
            producer.close();
        }
    }

}
