package io.github.quickmsg.core.http.actors;

import io.github.quickmsg.common.annotation.AllowCors;
import io.github.quickmsg.common.annotation.Header;
import io.github.quickmsg.common.annotation.Router;
import io.github.quickmsg.common.channel.MqttChannel;
import io.github.quickmsg.common.config.Configuration;
import io.github.quickmsg.common.context.ContextHolder;
import io.github.quickmsg.common.enums.HttpType;
import io.github.quickmsg.common.utils.JacksonUtil;
import io.github.quickmsg.core.http.AbstractHttpActor;
import io.github.quickmsg.core.http.HttpConfiguration;
import io.github.quickmsg.core.http.model.LoginDo;
import io.github.quickmsg.core.http.model.LoginVm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author luxurong
 */
@Router(value = "/smqtt/close/connection", type = HttpType.POST)
@Slf4j
@Header(key = "Content-Type", value = "application/json")
@AllowCors
public class CloseConnectionActor extends AbstractHttpActor {

    @Override
    public Publisher<Void> doRequest(HttpServerRequest request, HttpServerResponse response, Configuration httpConfiguration) {
        return request
                .receive()
                .asString(StandardCharsets.UTF_8)
                .map(this.toJson(Close.class))
                .doOnNext(close -> {
                    if(CollectionUtils.isNotEmpty(close.getIds())){
                        close.getIds().forEach(id->{
                            MqttChannel mqttChannel=ContextHolder.getReceiveContext()
                                    .getChannelRegistry()
                                    .get(id);
                            if(mqttChannel!=null){
                                mqttChannel.close().subscribe();
                            }
                        });
                    }
                }).then();
    }


    @Data
    public static class Close{
        private List<String> ids;
    }



}
