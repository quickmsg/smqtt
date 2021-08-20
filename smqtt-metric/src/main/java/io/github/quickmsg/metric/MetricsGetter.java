package io.github.quickmsg.metric;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author luxurong
 */
public interface MetricsGetter {

    /**
     * 获取统计信息
     *
     * @return {@link ObjectNode}
     */
    ObjectNode metrics();

}
