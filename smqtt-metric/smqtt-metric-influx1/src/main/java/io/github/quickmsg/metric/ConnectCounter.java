package io.github.quickmsg.metric;

import io.github.quickmsg.common.metric.MetricConstant;
import io.github.quickmsg.metric.api.WholeCounter;

/**
 * @author luxurong
 */
public class ConnectCounter extends WholeCounter {

    @Override
    public void callMeter(long counter) {
        Influx1Metric.INFLUX_METER_REGISTRY_INSTANCE.gauge(MetricConstant.CONNECT_COUNTER_NAME, counter);
    }
}
