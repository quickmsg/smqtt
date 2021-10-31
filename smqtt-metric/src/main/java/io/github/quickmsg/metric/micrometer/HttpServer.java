package io.github.quickmsg.metric.micrometer;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.exporter.common.TextFormat;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Random;

public class HttpServer {

    public static void main(String[] args) {
        PrometheusMeterRegistry prometheusRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        Metrics.globalRegistry.add(prometheusRegistry);

        Counter counter = Metrics.globalRegistry.counter("smqtt.http.request", "createOrder", "/order/create");
        Random r = new Random(1);

        try {
            com.sun.net.httpserver.HttpServer server = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(9191), 0);
            server.createContext("/scrape", httpExchange -> {
                int ran1 = r.nextInt(100);
                for (int i = 0; i < ran1; i++) {
                    counter.increment();
                }

                String response = prometheusRegistry.scrape(TextFormat.CONTENT_TYPE_OPENMETRICS_100);
                httpExchange.sendResponseHeaders(200, response.getBytes().length);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            });
            new Thread(server::start).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
