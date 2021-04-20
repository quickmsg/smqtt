package com.github.smqtt.server;

import com.github.smqtt.common.transport.Transport;
import com.github.smqtt.core.DefaultTransport;
import com.github.smqtt.core.minitor.DirectUsedMonitor;
import com.github.smqtt.core.mqtt.MqttConfiguration;
import com.github.smqtt.core.mqtt.MqttTransportFactory;
import com.github.smqtt.core.websocket.WebSocketMqttTransportFactory;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * @author luxurong
 * @date 2021/4/5 20:51
 * @description
 */
public class WebsocketTransportFactoryTest {




    @Test
    public void testWsTransport() throws InterruptedException {
        System.out.println(DefaultTransport.receiveContext);

        DirectUsedMonitor directUsedMonitor = new DirectUsedMonitor();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        MqttConfiguration mqttConfiguration = new MqttConfiguration();
        mqttConfiguration.setPort(8998);
        WebSocketMqttTransportFactory mqttTransportFactory = new WebSocketMqttTransportFactory();
        Transport transport1= mqttTransportFactory.createTransport(mqttConfiguration)
                .start()
                .doOnError(Throwable::printStackTrace)
                .block();
        System.out.println(transport1);
        directUsedMonitor.startMonitor();
        countDownLatch.await();
    }

}
