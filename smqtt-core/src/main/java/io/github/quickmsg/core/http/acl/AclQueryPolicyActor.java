package io.github.quickmsg.core.http.acl;

import io.github.quickmsg.common.acl.model.PolicyModel;
import io.github.quickmsg.common.annotation.AllowCors;
import io.github.quickmsg.common.annotation.Header;
import io.github.quickmsg.common.annotation.Router;
import io.github.quickmsg.common.config.Configuration;
import io.github.quickmsg.common.context.ContextHolder;
import io.github.quickmsg.common.enums.HttpType;
import io.github.quickmsg.common.utils.JacksonUtil;
import io.github.quickmsg.core.http.AbstractHttpActor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import java.nio.charset.StandardCharsets;

/**
 * @author luxurong
 */

@Router(value = "/smqtt/acl/policy/query", type = HttpType.POST)
@Slf4j
@Header(key = "Content-Type", value = "application/json")
@AllowCors
public class AclQueryPolicyActor extends AbstractHttpActor {

    @Override
    public Publisher<Void> doRequest(HttpServerRequest request, HttpServerResponse response, Configuration configuration) {
        return request
                .receive()
                .asString(StandardCharsets.UTF_8)
                .map(this.toJson(PolicyModel.class))
                .doOnNext(policyModel ->
                        response.sendString(Mono.just(JacksonUtil.bean2Json(ContextHolder.getReceiveContext().getAclManager().get(policyModel)))).then().subscribe()
                )
                .then();
    }

}
