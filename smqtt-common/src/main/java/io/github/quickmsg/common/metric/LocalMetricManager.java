package io.github.quickmsg.common.metric;

import io.github.quickmsg.common.config.BootstrapConfig;

/**
 * @author luxurong
 */
public class LocalMetricManager implements MetricManager {

    @Override
    public MetricRegistry getMetricRegistry() {
        return new MetricRegistry() {
            @Override
            public WholeCounter getTopicCounter() {
                return new WholeCounter(getMetricBean()) {
                    @Override
                    public void callMeter(long counter) {

                    }
                };
            }

            @Override
            public WholeCounter getConnectCounter() {
                return new WholeCounter(getMetricBean()) {
                    @Override
                    public void callMeter(long counter) {

                    }
                };
            }

            @Override
            public WindowCounter getReadCounter() {
                return new ReadCounter(getMetricBean());
            }

            @Override
            public WindowCounter getWriteCounter() {
                return new WriteCounter(getMetricBean());
            }

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
