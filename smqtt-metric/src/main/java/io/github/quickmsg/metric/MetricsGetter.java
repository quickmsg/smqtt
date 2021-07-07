package io.github.quickmsg.metric;

import com.alibaba.fastjson.JSONObject;

/**
 * @author luxurong
 */
public interface MetricsGetter {

    /**
     * 获取统计信息
     *
     * @return {@link JSONObject}
     */
    JSONObject metrics();

}
