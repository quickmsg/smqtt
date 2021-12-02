package io.github.quickmsg.common.metric;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;

/**
 * @author luxurong
 */
public interface MetricBean {

    MetricBean Close();

    MeterRegistry getMeterRegistry();

    default Tags getTags() {
        return Tags.empty().and(MetricConstant.COMMON_TAG_NAME, MetricConstant.COMMON_TAG_VALUE);
    }
}
