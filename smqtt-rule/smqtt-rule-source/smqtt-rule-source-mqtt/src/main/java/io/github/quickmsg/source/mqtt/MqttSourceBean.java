package io.github.quickmsg.source.mqtt;

import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import com.hivemq.client.mqtt.mqtt3.lifecycle.Mqtt3ClientDisconnectedContext;
import com.hivemq.client.mqtt.mqtt3.message.connect.Mqtt3ConnectBuilder;
import com.hivemq.client.mqtt.mqtt3.message.connect.connack.Mqtt3ConnAck;
import io.github.quickmsg.common.rule.source.Source;
import io.github.quickmsg.common.rule.source.SourceBean;
import io.github.quickmsg.common.utils.JacksonUtil;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;


/**
 * mqtt
 *
 * @author zhaopeng
 */
@Slf4j
public class MqttSourceBean implements SourceBean {

    private Mqtt3AsyncClient client;

    @Override
    public Boolean support(Source source) {
        return source == Source.MQTT;
    }

    /**
     * 初始化
     *
     * @param sourceParam 参数
     * @return boolean
     */
    @Override
    public Boolean bootstrap(Map<String, Object> sourceParam) {
        try {
            String clientId = sourceParam.get("clientId").toString();
            String host = sourceParam.get("host").toString();
            Integer port = Integer.parseInt(sourceParam.get("port").toString());

            client = MqttClient.builder()
                    .useMqttVersion3()
                    .identifier(clientId)
                    .serverHost(host)
                    .serverPort(port)
                    .addDisconnectedListener(context -> {
                        context.getReconnector()
                                .reconnect(true) // always reconnect (includes calling disconnect)
                                .delay(2L * context.getReconnector().getAttempts(), TimeUnit.SECONDS); // linear scaling delay
                    })
                    .addDisconnectedListener(context -> {
                        final Mqtt3ClientDisconnectedContext context3 = (Mqtt3ClientDisconnectedContext) context;
                        String userName = sourceParam.get("userName").toString();
                        String passWord = sourceParam.get("passWord").toString();
                        if (!StringUtil.isNullOrEmpty(userName) && !StringUtil.isNullOrEmpty(passWord)) {
                            context3.getReconnector()
                                    .connectWith()
                                    .simpleAuth()
                                    .username(userName)
                                    .password(passWord.getBytes())
                                    .applySimpleAuth()
                                    .applyConnect();
                        }
                    })
                    .addConnectedListener(context -> log.info("mqtt client connected " + LocalTime.now()))
                    .addDisconnectedListener(context -> log.error("mqtt client disconnected " + LocalTime.now()))
                    .buildAsync();

            Mqtt3ConnectBuilder.Send<CompletableFuture<Mqtt3ConnAck>> completableFutureSend = client.connectWith();

            if (sourceParam.get("userName") != null && sourceParam.get("passWord") != null) {
                String userName = sourceParam.get("userName").toString();
                String passWord = sourceParam.get("passWord").toString();
                if (!StringUtil.isNullOrEmpty(userName) && !StringUtil.isNullOrEmpty(passWord)) {
                    completableFutureSend.simpleAuth()
                            .username(userName)
                            .password(passWord.getBytes())
                            .applySimpleAuth();
                }
            }

            completableFutureSend
                    .send()
                    .whenComplete((connAck, throwable) -> {
                        if (throwable != null) {
                            // handle failure
                            log.error("mqtt client connect error", throwable);
                        } else {
                            // setup subscribes or start publishing
                        }
                    });

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
        String topic = (String) object.get("topic");
        Object msg = object.get("msg");
        String bytes = msg instanceof Map ? JacksonUtil.map2Json((Map<? extends Object, ? extends Object>) msg) : msg.toString();
        Boolean retain = (Boolean) object.get("retain");
        Integer qos = Optional.ofNullable((Integer) object.get("qos")).orElse(0);
        client.publishWith()
                .topic(topic)
                .payload(bytes.getBytes())
                .qos(Objects.requireNonNull(MqttQos.fromCode(qos)))
                .retain(retain)
                .send()
                .whenComplete((publish, throwable) -> {
                    if (throwable != null) {
                        // handle failure to publish
                        log.error("mqtt client publish error", throwable);
                    }
                });
    }


    @Override
    public void close() {
        if (client != null) {
            client.disconnect();
        }
    }

    private static CompletableFuture<byte[]> getOAuthToken() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                for (int i = 0; i < 5; i++) {
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println("OAuth server is slow to respond ...");
                }
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
            return new byte[]{1, 2, 3};
        });
    }
}
