package io.github.quickmsg.source.rocketmq;

import io.github.quickmsg.common.rule.source.Source;
import io.github.quickmsg.common.rule.source.SourceBean;
import io.github.quickmsg.common.utils.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.util.Map;

/**
 * rocketmq source
 *
 * @author zhaopeng
 * @date 2021/09/16
 */
@Slf4j
public class RocketmqSourceBean implements SourceBean {

    /**
     * 生产者
     */
    private DefaultMQProducer producer;

    private String topic;

    private String tags;

    @Override
    public Boolean support(Source source) {
        return source == Source.ROCKET_MQ;
    }


    /**
     * 初始化rocketmq
     *
     * @param sourceParam 参数
     * @return {@link Boolean}
     */
    @Override
    public Boolean bootstrap(Map<String, Object> sourceParam) {
        try {
            topic = sourceParam.get("topic").toString();
            tags = sourceParam.get("tags").toString();

            producer = new DefaultMQProducer(sourceParam.get("producerGroup").toString());
            // 设置NameServer地址
            producer.setNamesrvAddr(sourceParam.get("namesrvAddr").toString());
            // 设置生产者实例名称
            producer.setInstanceName(sourceParam.get("instanceName").toString());
            // 启动生产者
            producer.start();
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
    public void transmit(Map<String, Object> object) {
        String json = JacksonUtil.bean2Json(object);
        log.info("kafka object {}", json);
        if (producer != null) {
            Message message = new Message(topic, tags, json.getBytes());
            //发送消息v
            try {
                SendResult sendResult = producer.send(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void close() {
        if (producer != null) {
            producer.shutdown();
        }
    }

}
