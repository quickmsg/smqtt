package io.github.quickmsg.source.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import io.github.quickmsg.common.rule.source.Source;
import io.github.quickmsg.common.rule.source.SourceBean;
import io.github.quickmsg.common.utils.JacksonUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * rabbitmq source
 *
 * @author leafseelight
 */
@Slf4j
public class RabbitmqSourceBean implements SourceBean {

    /**
     * 连接对象
     */
    private Connection connection = null;
    /**
     * 缓存消息队列
     */
    private Map<String, Channel> channelHashMap = new HashMap<>();

    /**
     * 队列名称
     */
    private String queueName;

    @Override
    public Boolean support(Source source) {
        return source == Source.RABBIT_MQ;
    }

    /**
     * 初始化rocketmq
     *
     * @param sourceParam 参数
     * @return Boolean
     */
    @Override
    public Boolean bootstrap(Map<String, Object> sourceParam) {
        try {
            //创建连接工厂
            ConnectionFactory factory = new ConnectionFactory();
            factory.setAutomaticRecoveryEnabled(true);
            factory.setTopologyRecoveryEnabled(true);
            factory.setNetworkRecoveryInterval(3000);
            //设置RabbitMQ相关信息
            factory.setHost(sourceParam.get("host").toString());
            factory.setPort(Integer.parseInt(sourceParam.get("port").toString()));
            factory.setUsername(sourceParam.get("userName").toString());
            factory.setPassword(sourceParam.get("passWord").toString());
            //创建一个新的连接
            connection = factory.newConnection();
            queueName = sourceParam.get("queueName").toString();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 转发数据
     * @param object 对象
     */
    @Override
    public void transmit(Object object) {
        corePublish(queueName, JacksonUtil.dynamicJson(object));
    }

    /**
     * 核心执行内容
     *
     * @param queueName 队列名
     * @param json body
     */
    public void corePublish(String queueName, String json) {
        try {
            Channel cacheChannel = channelHashMap.get(queueName);
            Channel channel = null;
            if (cacheChannel == null) {
                //创建一个通道
                channel = connection.createChannel();
                channelHashMap.put(queueName, channel);
            } else {
                channel = cacheChannel;
            }
            // 声明一个队列
            channel.queueDeclare(queueName, false, false, false, null);
            // 发送消息到队列中
            channel.basicPublish("", queueName, null, json.getBytes("UTF-8"));
        }catch (Exception e){
            log.error("RabbitMq转发异常",e);
        }
    }


    @Override
    public void close() {
        try {
            //关闭通道和连接
            for (Map.Entry<String, Channel> stringChannelEntry : channelHashMap.entrySet()) {
                Channel channel = stringChannelEntry.getValue();
                channel.close();
            }
            connection.close();
        } catch (Exception e) {
            log.error("#Close.Exception: {}", e.getMessage());
        }
    }


}
