package io.github.quickmsg.core.http.actors;

import io.github.quickmsg.common.annotation.AllowCors;
import io.github.quickmsg.common.annotation.Header;
import io.github.quickmsg.common.annotation.Router;
import io.github.quickmsg.common.config.Configuration;
import io.github.quickmsg.common.enums.HttpType;
import io.github.quickmsg.common.http.HttpActor;
import io.github.quickmsg.common.metric.MetricManagerHolder;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

/**
 * 监控指标
 *
 * @author easy
 * @date 2021/10/30
 */
@Router(value = "/smqtt/meter", type = HttpType.GET)
@Slf4j
@Header(key = "Content-Type", value = "text/plain; version=0.0.4;charset=utf-8")
@AllowCors
public class PrometheusActor implements HttpActor {


    @Override
    public Publisher<Void> doRequest(HttpServerRequest request, HttpServerResponse response, Configuration configuration) {
        return request
                .receive()
                .then(response
                        .sendString(Mono.just(MetricManagerHolder.getMetricManager().scrape()))
                        .then());
    }
}
