package io.github.quickmsg.common.metric;

/**
 * @author luxurong
 */
public class TotalCounter extends WholeCounter {

    private final CounterType counterType;

    public TotalCounter(MetricBean metricBean, CounterType counterType) {
        super(metricBean);
        this.counterType = counterType;
        initCount();
    }


    @Override
    public void callMeter(long counter) {
        getMetricBean().getMeterRegistry().gauge(getCounterType().getDesc(), counter);
    }

    @Override
    public CounterType getCounterType() {
        return this.counterType;
    }
}
