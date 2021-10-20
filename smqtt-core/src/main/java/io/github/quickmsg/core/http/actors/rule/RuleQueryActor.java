package io.github.quickmsg.core.http.actors.rule;

import io.github.quickmsg.common.annotation.AllowCors;
import io.github.quickmsg.common.annotation.Header;
import io.github.quickmsg.common.annotation.Router;
import io.github.quickmsg.common.config.Configuration;
import io.github.quickmsg.common.enums.HttpType;
import io.github.quickmsg.common.enums.RuleType;
import io.github.quickmsg.common.rule.RuleChainDefinition;
import io.github.quickmsg.common.rule.RuleDefinition;
import io.github.quickmsg.common.utils.JacksonUtil;
import io.github.quickmsg.core.http.AbstractHttpActor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import java.util.ArrayList;
import java.util.List;


/**
 * 规则查询
 *
 * @author zhaopeng
 * @date 2021/10/20
 */
@Router(value = "/smqtt/ruleQuery", type = HttpType.POST)
@Slf4j
@Header(key = "Content-Type", value = "application/json")
@AllowCors
public class RuleQueryActor extends AbstractHttpActor {

    @Override
    public Publisher<Void> doRequest(HttpServerRequest request, HttpServerResponse response, Configuration httpConfiguration) {
        List<RuleChainDefinition> list = new ArrayList<RuleChainDefinition>();

        RuleChainDefinition ruleChainDefinition = new RuleChainDefinition();
        ruleChainDefinition.setRuleName("A1");

        List<RuleDefinition> chain = new ArrayList<RuleDefinition>();

        RuleDefinition ruleDefinition = new RuleDefinition();
        ruleDefinition.setRuleName("R1");
        ruleDefinition.setRuleType(RuleType.KAFKA);
        ruleDefinition.setScript("exe");

        RuleDefinition ruleDefinition2 = new RuleDefinition();
        ruleDefinition2.setRuleName("R2");
        ruleDefinition2.setRuleType(RuleType.KAFKA);
        ruleDefinition2.setScript("exe2");

        chain.add(ruleDefinition);
        chain.add(ruleDefinition2);
        ruleChainDefinition.setChain(chain);

        RuleChainDefinition ruleChainDefinition2 = new RuleChainDefinition();
        ruleChainDefinition2.setRuleName("AA1");

        List<RuleDefinition> chain2 = new ArrayList<RuleDefinition>();

        RuleDefinition ruleDefinition22 = new RuleDefinition();
        ruleDefinition22.setRuleName("R1");
        ruleDefinition22.setRuleType(RuleType.KAFKA);
        ruleDefinition22.setScript("exe");

        RuleDefinition ruleDefinition222 = new RuleDefinition();
        ruleDefinition222.setRuleName("R2");
        ruleDefinition222.setRuleType(RuleType.KAFKA);
        ruleDefinition222.setScript("exe2");

        chain2.add(ruleDefinition22);
        chain2.add(ruleDefinition222);
        ruleChainDefinition2.setChain(chain2);

        list.add(ruleChainDefinition);
        list.add(ruleChainDefinition2);

        return request
                .receive()
                .then(response
                        .sendString(Mono.just(JacksonUtil.bean2Json(list)))
                        .then());
    }

}
