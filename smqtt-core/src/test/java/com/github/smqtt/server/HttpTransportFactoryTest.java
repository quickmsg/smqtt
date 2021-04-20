package com.github.smqtt.server;

import com.github.smqtt.common.transport.Transport;
import com.github.smqtt.core.DefaultTransport;
import com.github.smqtt.core.http.HttpConfiguration;
import com.github.smqtt.core.http.HttpTransportFactory;
import com.github.smqtt.core.minitor.DirectUsedMonitor;
import com.github.smqtt.core.mqtt.MqttConfiguration;
import com.github.smqtt.core.mqtt.MqttTransportFactory;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * @author luxurong
 * @date 2021/4/5 20:51
 * @description
 */
public class HttpTransportFactoryTest {


    @Test
    public void testTransport() throws InterruptedException {
        DirectUsedMonitor directUsedMonitor = new DirectUsedMonitor();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        HttpConfiguration httpConfiguration = new HttpConfiguration();
        httpConfiguration.setPort(18997);
        httpConfiguration.setAccessLog(true);
        httpConfiguration.setWiretap(true);
        HttpTransportFactory httpTransportFactory = new HttpTransportFactory();
        Transport transport1= httpTransportFactory.createTransport(httpConfiguration)
                .start()
                .doOnError(Throwable::printStackTrace)
                .block();
        directUsedMonitor.startMonitor();

        countDownLatch.await();
    }



}
