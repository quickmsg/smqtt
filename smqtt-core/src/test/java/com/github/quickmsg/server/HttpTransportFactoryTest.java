package com.github.quickmsg.server;

import com.github.quickmsg.core.http.HttpConfiguration;
import com.github.quickmsg.core.http.HttpTransportFactory;
import com.github.quickmsg.common.transport.Transport;
import com.github.quickmsg.core.minitor.DirectUsedMonitor;
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
