package com.github.smqtt.server;

import com.github.smqtt.core.mqtt.MqttConfiguration;
import com.github.smqtt.core.mqtt.MqttTransportFactory;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * @author luxurong
 * @date 2021/4/5 20:51
 * @description
 */
public class TransportFactoryTest {


    @Test
    public void testTransport() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        MqttConfiguration mqttConfiguration =new MqttConfiguration();
        mqttConfiguration.setPort(8999);
        MqttTransportFactory mqttTransportFactory = new MqttTransportFactory();
        mqttTransportFactory.createTransport(mqttConfiguration)
                .start()
                .doOnError(Throwable::printStackTrace)
                .block();
        countDownLatch.await();
    }

}
