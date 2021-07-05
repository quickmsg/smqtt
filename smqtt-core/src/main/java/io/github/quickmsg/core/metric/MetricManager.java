package io.github.quickmsg.core.metric;

import io.github.quickmsg.core.counter.SideWindowCounter;
import io.github.quickmsg.core.counter.WindowCounter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author luxurong
 */
@Getter
@Slf4j
public class MetricManager {


    private static WindowCounter readAllBufferSize = new SideWindowCounter(1, TimeUnit.HOURS, "TRANSPORT-READ-BUFFER-SIZE");

    private static WindowCounter writeAllBufferSize = new SideWindowCounter(1, TimeUnit.HOURS, "TRANSPORT-WRITE-BUFFER-SIZE");

    private static WindowCounter connectCounter = new SideWindowCounter(1, TimeUnit.HOURS, "CONNECT-SIZE");


    private static Map<String, WindowCounter> transportBufferSize = new ConcurrentHashMap<>();

    public static void recordDataSend(Integer bufferSize) {
        writeAllBufferSize.apply(bufferSize);
    }

    public static void recordDataReceived(Integer bufferSize) {
        readAllBufferSize.apply(bufferSize);
    }

    public static void recordConnect(Integer size) {
        connectCounter.apply(size);
    }

    public static void acceptMetric(String topic, Integer bufferSize) {
        WindowCounter windowCounter = transportBufferSize.computeIfAbsent(topic, tc -> new SideWindowCounter(1, TimeUnit.MINUTES, tc));
        windowCounter.apply(bufferSize);
    }


}
