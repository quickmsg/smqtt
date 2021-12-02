package io.github.quickmsg.metric;

import io.github.quickmsg.metric.api.MetricBean;
import io.github.quickmsg.metric.api.MetricConstant;
import io.github.quickmsg.metric.api.WholeCounter;

/**
 * @author luxurong
 */
public class TopicCounter extends WholeCounter {

    public TopicCounter(MetricBean metricBean) {
        super(metricBean);
    }

    @Override
    public void callMeter(long counter) {
        getMetricBean().getMeterRegistry().gauge(MetricConstant.TOPIC_COUNTER_NAME, counter);
    }
}
