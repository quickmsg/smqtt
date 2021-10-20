package io.github.quickmsg.core.http.actors.source;

import io.github.quickmsg.common.annotation.AllowCors;
import io.github.quickmsg.common.annotation.Header;
import io.github.quickmsg.common.annotation.Router;
import io.github.quickmsg.common.config.Configuration;
import io.github.quickmsg.common.enums.HttpType;
import io.github.quickmsg.common.rule.source.Source;
import io.github.quickmsg.common.rule.source.SourceDefinition;
import io.github.quickmsg.common.utils.JacksonUtil;
import io.github.quickmsg.core.http.AbstractHttpActor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 数据源查询
 *
 * @author zhaopeng
 * @date 2021/10/20
 */
@Router(value = "/smqtt/sourceQuery", type = HttpType.POST)
@Slf4j
@Header(key = "Content-Type", value = "application/json")
@AllowCors
public class SourceQueryActor extends AbstractHttpActor {

    @Override
    public Publisher<Void> doRequest(HttpServerRequest request, HttpServerResponse response, Configuration httpConfiguration) {
        SourceDefinition sourceDefinition = new SourceDefinition();
        sourceDefinition.setSourceName("S1");
        sourceDefinition.setSource(Source.KAFKA);

        Map<String, Object> sourceAttributes = new HashMap<>();
        sourceAttributes.put("host", "110");

        sourceDefinition.setSourceAttributes(sourceAttributes);

        SourceDefinition sourceDefinition2 = new SourceDefinition();
        sourceDefinition2.setSourceName("S1");
        sourceDefinition2.setSource(Source.KAFKA);

        Map<String, Object> sourceAttributes2 = new HashMap<>();
        sourceAttributes2.put("host", "111");

        sourceDefinition2.setSourceAttributes(sourceAttributes2);

        List<SourceDefinition> list = new ArrayList<>();
        list.add(sourceDefinition);
        list.add(sourceDefinition2);

        return request
                .receive()
                .then(response
                        .sendString(Mono.just(JacksonUtil.bean2Json(list)))
                        .then());
    }

}
