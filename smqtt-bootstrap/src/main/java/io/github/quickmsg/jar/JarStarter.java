package io.github.quickmsg.jar;

import io.github.quickmsg.AbstractStarter;
import io.github.quickmsg.metric.micrometer.PrometheusMeterRegistrySingleton;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Random;

/**
 * @author luxurong
 */
@Slf4j
public class JarStarter extends AbstractStarter {

    public static void main(String[] args) throws IOException {

        PrometheusMeterRegistry prometheusRegistry = PrometheusMeterRegistrySingleton.getInstance().getPrometheusMeterRegistry();
        Metrics.globalRegistry.add(prometheusRegistry);

        Counter counter = prometheusRegistry.counter("smqtt.http.request", "createOrder", "/order/create");
        Random r = new Random(1);

        int ran1 = r.nextInt(100);
        for (int i = 0; i < ran1; i++) {
            counter.increment();
        }

        log.info("JarStarter start args {}", String.join(",", args));
        if (args.length > 0) {
            start(args[0]);
        } else {
            start(null);
        }

    }
}
