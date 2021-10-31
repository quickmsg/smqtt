package io.github.quickmsg.metric.micrometer;

import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;

public class PrometheusMeterRegistrySingleton {

    private static PrometheusMeterRegistrySingleton instance;

    private PrometheusMeterRegistry prometheusMeterRegistry = null;

    private PrometheusMeterRegistrySingleton() {
        this.prometheusMeterRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
    }

    public static synchronized PrometheusMeterRegistrySingleton getInstance() {
        if (instance == null) {
            instance = new PrometheusMeterRegistrySingleton();
        }
        return instance;
    }

    public PrometheusMeterRegistry getPrometheusMeterRegistry() {
        return prometheusMeterRegistry;
    }
}
