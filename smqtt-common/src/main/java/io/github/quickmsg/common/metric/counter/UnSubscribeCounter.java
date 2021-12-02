package io.github.quickmsg.common.metric.counter;

import io.github.quickmsg.common.metric.CounterType;
import io.github.quickmsg.common.metric.MetricBean;
import io.github.quickmsg.common.metric.WholeCounter;
import io.micrometer.core.instrument.Counter;

/**
 * @author luxurong
 */
public class UnSubscribeCounter extends WholeCounter {

    private final Counter counter;

    public UnSubscribeCounter(MetricBean metricBean) {
        super(metricBean);
        this.counter =
                getMetricBean()
                        .getMeterRegistry().counter(getCounterType().getDesc(), getMetricBean().getTags());
    }

    @Override
    public void callMeter(long count) {
        counter.increment();
    }

    @Override
    public CounterType getCounterType() {
        return CounterType.UN_SUBSCRIBE;
    }
}
