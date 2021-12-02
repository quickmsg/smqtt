package io.github.quickmsg.common.metric;

import io.github.quickmsg.common.config.BootstrapConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @author luxurong
 */
public class LocalMetricManager implements MetricManager {

    public LocalMetricManager() {
    }


    @Override
    public MetricRegistry getMetricRegistry() {
        List<MetricCounter> counters = new ArrayList<>();
        counters.add(new WholeCounter(getMetricBean()) {
            @Override
            public void callMeter(long counter) {

            }

            @Override
            public CounterType getCounterType() {
                return CounterType.CONNECT;
            }
        });
        counters.add(new WholeCounter(getMetricBean()) {
            @Override
            public void callMeter(long counter) {

            }

            @Override
            public CounterType getCounterType() {
                return CounterType.DIS_CONNECT;
            }
        });
        counters.add(new WholeCounter(getMetricBean()) {
            @Override
            public void callMeter(long counter) {

            }

            @Override
            public CounterType getCounterType() {
                return CounterType.SUBSCRIBE;
            }
        });
        counters.add(new WholeCounter(getMetricBean()) {
            @Override
            public void callMeter(long counter) {

            }

            @Override
            public CounterType getCounterType() {
                return CounterType.UN_SUBSCRIBE;
            }
        });
        counters.add(new WholeCounter(getMetricBean()) {
            @Override
            public void callMeter(long counter) {

            }

            @Override
            public CounterType getCounterType() {
                return CounterType.PUBLISH;
            }
        });
        counters.add(new ReadCounter(getMetricBean()));
        counters.add(new WriteCounter(getMetricBean()));
        return new AbstractMetricRegistry(counters) {
        };
    }

    @Override
    public MetricBean getMetricBean() {
        return null;
    }

    @Override
    public BootstrapConfig.MeterConfig getMeterConfig() {
        return null;
    }
}
