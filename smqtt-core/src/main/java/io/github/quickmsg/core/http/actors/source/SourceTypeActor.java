package io.github.quickmsg.core.http.actors.source;

import io.github.quickmsg.common.annotation.AllowCors;
import io.github.quickmsg.common.annotation.Header;
import io.github.quickmsg.common.annotation.Router;
import io.github.quickmsg.common.config.Configuration;
import io.github.quickmsg.common.enums.HttpType;
import io.github.quickmsg.common.rule.source.Source;
import io.github.quickmsg.common.utils.JacksonUtil;
import io.github.quickmsg.core.http.AbstractHttpActor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import java.util.Arrays;
import java.util.List;


/**
 * 数据源枚举
 *
 * @author zhaopeng
 * @date 2021/10/24
 */
@Router(value = "/smqtt/sourceType", type = HttpType.GET)
@Slf4j
@Header(key = "Content-Type", value = "application/json")
@AllowCors
public class SourceTypeActor extends AbstractHttpActor {

    @Override
    public Publisher<Void> doRequest(HttpServerRequest request, HttpServerResponse response, Configuration httpConfiguration) {
        List<Source> list = Arrays.asList(Source.values());
        return request
                .receive()
                .then(response
                        .sendString(Mono.just(JacksonUtil.bean2Json(list)))
                        .then());
    }

}
