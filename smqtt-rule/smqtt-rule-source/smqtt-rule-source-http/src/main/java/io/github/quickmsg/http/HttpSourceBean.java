package io.github.quickmsg.http;

import io.github.quickmsg.common.rule.source.Source;
import io.github.quickmsg.common.rule.source.SourceBean;

import java.util.Map;

/**
 * @author luxurong
 * @date 2021/9/15 14:07
 */
public class HttpSourceBean implements SourceBean {


    @Override
    public Boolean support(Source source) {
        return source == Source.HTTP;
    }

    @Override
    public Boolean bootstrap(Map<String, Object> sourceParam) {
        return null;
    }

    @Override
    public Object transmit(Object object) {
        return null;
    }

    @Override
    public void close() {

    }

}
