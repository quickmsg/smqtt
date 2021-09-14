package io.github.quickmsg.common.rule.source;

import io.github.quickmsg.common.rule.Source;
import io.github.quickmsg.common.spi.DynamicLoader;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author luxurong
 * @date 2021/8/24 16:41
 */
public interface SourceBean {


    List<SourceBean> SOURCE_BEAN_LIST = DynamicLoader.findAll(SourceBean.class)
            .collect(Collectors.toList());


    /**
     * 是否支持source
     *
     * @param source {@link Source}
     * @return Boolean
     */
    Boolean support(Source source);


    /**
     * 启动source
     *
     * @param sourceParam 请求参数
     * @return Boolean
     */
    Boolean bootstrap(Map<String, Object> sourceParam);


    /**
     * 转发数据
     *
     * @param object {@link Source}
     * @return Object
     */
    Object transmit(Object object);

    /**
     * 关闭
     *
     * @return {@link Boolean}
     */
    Boolean close(Map<String, Object> sourceParam);

}
