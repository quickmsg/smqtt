package io.github.quickmsg.core.http.actors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.github.quickmsg.common.annotation.AllowCors;
import io.github.quickmsg.common.annotation.Header;
import io.github.quickmsg.common.annotation.Router;
import io.github.quickmsg.common.config.Configuration;
import io.github.quickmsg.common.http.HttpActor;
import io.github.quickmsg.common.enums.HttpType;
import io.github.quickmsg.core.DefaultTransport;
import io.github.quickmsg.core.http.HttpConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

/**
 * @author luxurong
 */
@Router(value = "/smqtt/cluster", type = HttpType.POST)
@Slf4j
@Header(key = "Content-Type", value = "application/json")
@AllowCors
public class ClusterActor implements HttpActor {


    @Override
    public Publisher<Void> doRequest(HttpServerRequest request, HttpServerResponse response, Configuration httpConfiguration) {
        return request
                .receive()
                .then(response.sendString(Mono.just(JSON.toJSONString(DefaultTransport.receiveContext.getClusterRegistry().getClusterNode(), SerializerFeature.DisableCircularReferenceDetect))).then());
    }
}
