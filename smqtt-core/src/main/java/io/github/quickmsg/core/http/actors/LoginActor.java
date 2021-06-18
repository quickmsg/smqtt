package io.github.quickmsg.core.http.actors;

import com.alibaba.fastjson.JSON;
import io.github.quickmsg.common.annotation.Header;
import io.github.quickmsg.common.annotation.Router;
import io.github.quickmsg.common.enums.HttpType;
import io.github.quickmsg.common.http.HttpActor;
import io.github.quickmsg.core.http.model.LoginDo;
import io.github.quickmsg.core.http.model.LoginVm;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * @author luxurong
 */
@Router(value = "/auth/login", type = HttpType.POST)
@Slf4j
@Header(key = "Content-Type", value = "application/json")
public class LoginActor implements HttpActor {

    @Override
    public Publisher<Void> doRequest(HttpServerRequest request, HttpServerResponse response) {

        return request
                .receive()
                .asString()
                .map(this.toJson(LoginDo.class))
                .doOnNext(loginDo -> {
                    if (this.validateLogin(loginDo)) {
                        LoginVm loginVm = new LoginVm();
                        loginVm.setAccessToken("jhbsadhjbajhdbjhabdsjhahjbsdjhbajsdbjhahjsdb");
                        loginVm.setExpiresIn(System.currentTimeMillis() + 100000000000000L);
                        Map<String, LoginVm> res = new HashMap<>();
                        res.put("data", loginVm);
                        response.sendString(Mono.just(JSON.toJSONString(res))).then().subscribe();
                    } else {
                        response.status(HttpResponseStatus.UNAUTHORIZED);
                        response.send().then().subscribe();
                    }
                }).then();

    }

    private boolean validateLogin(LoginDo loginDo) {
        return "smqtt".equals(loginDo.getUsername()) && "smqtt".equals(loginDo.getPassword());
    }
}
