package io.github.quickmsg.metric;

import io.github.quickmsg.metric.api.MetricBean;
import io.github.quickmsg.metric.api.MetricConstant;
import io.github.quickmsg.metric.api.WholeCounter;

/**
 * @author luxurong
 */
public class ConnectCounter extends WholeCounter {

    public ConnectCounter(MetricBean metricBean) {
        super(metricBean);
    }

    @Override
    public void callMeter(long counter) {
        getMetricBean().getMeterRegistry().gauge(MetricConstant.CONNECT_COUNTER_NAME, counter);
    }

}
