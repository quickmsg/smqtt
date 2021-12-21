package io.github.quickmsg.common.message;

import io.github.quickmsg.common.utils.JacksonUtil;

import java.util.HashMap;

/**
 * @author luxurong
 */
public class JsonMap<K,V> extends HashMap<K,V> {

    public JsonMap() {
        super();
    }


    public JsonMap(int initialCapacity) {
        super(initialCapacity);
    }

    @Override
    public String toString() {
        return JacksonUtil.map2Json(this);
    }
}
