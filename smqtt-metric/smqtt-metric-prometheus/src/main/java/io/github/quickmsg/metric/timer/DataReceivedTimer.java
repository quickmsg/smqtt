package io.github.quickmsg.metric.timer;

import io.github.quickmsg.common.metric.MetricTimer;
import io.github.quickmsg.metric.PrometheusMetric;
import io.micrometer.core.instrument.Timer;

public class DataReceivedTimer implements MetricTimer {


    public void aa() {
        Timer timer = Timer
                .builder("my.timer")
                .description("a description of what this timer does") // optional
                .tags("region", "test") // optional
                .register(PrometheusMetric.PROMETHEUS_METER_REGISTRY_INSTANCE);
    }

}
