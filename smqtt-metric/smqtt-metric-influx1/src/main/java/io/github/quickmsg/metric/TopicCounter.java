package io.github.quickmsg.metric;

import io.github.quickmsg.common.metric.MetricBean;
import io.github.quickmsg.common.metric.MetricConstant;
import io.github.quickmsg.common.metric.WholeCounter;

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
