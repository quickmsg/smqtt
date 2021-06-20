package io.github.quickmsg.core.metric;

import io.github.quickmsg.core.counter.SideWindowCounter;
import io.github.quickmsg.core.counter.WindowCounter;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author luxurong
 */
@Getter
@Slf4j
public class MetricManager {

    private static WindowCounter transportBufferSize = new SideWindowCounter(10, TimeUnit.SECONDS, "TRANSPORT-BUFFER-SIZE");

    public static void registryMetric(MqttPublishMessage mqttPublishMessage) {
        transportBufferSize.apply(mqttPublishMessage.payload().readableBytes());
    }

}
