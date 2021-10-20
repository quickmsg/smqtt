package io.github.quickmsg.common.http;

import io.github.quickmsg.common.config.Configuration;
import io.github.quickmsg.common.spi.DynamicLoader;
import io.github.quickmsg.common.utils.JacksonUtil;
import org.reactivestreams.Publisher;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author luxurong
 */
public interface HttpActor {


    List<HttpActor> INSTANCE = DynamicLoader.findAll(HttpActor.class).collect(Collectors.toList());


    /**
     * 处理
     * 如何要处理GET请求query param reactor-netty是不支持的，需要自己去处理
     * QueryStringDecoder queryStringDecoder  = new QueryStringDecoder(request.uri());
     *
     * @param request  {@link HttpServerRequest}
     * @param response {@link HttpServerResponse}
     * @param configuration {@link Configuration}
     * @return Object
     */
    Publisher<Void> doRequest(HttpServerRequest request, HttpServerResponse response, Configuration configuration);


    /**
     * json转换器
     *
     * @param tClass class
     * @param <T>    返回类型
     * @return {{@link Function}
     */
    default <T> Function<String, T> toJson(Class<T> tClass) {
        return message -> JacksonUtil.json2Bean(message, tClass);
    }

    /**
     * json转换器
     *
     * @param tClass class
     * @param <T>    返回类型
     * @return {{@link Function}
     */
    default <T> Function<String, List<T>> toList(Class<T> tClass) {
        return message -> JacksonUtil.json2List(message, tClass);
    }


}
