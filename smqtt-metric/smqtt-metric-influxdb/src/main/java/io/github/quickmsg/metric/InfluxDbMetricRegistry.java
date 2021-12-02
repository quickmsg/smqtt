package io.github.quickmsg.metric;

import io.github.quickmsg.common.metric.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author luxurong
 */
public class InfluxDbMetricRegistry extends AbstractMetricRegistry {


    public InfluxDbMetricRegistry( List<MetricCounter> counters) {
        super(counters);
    }
}
