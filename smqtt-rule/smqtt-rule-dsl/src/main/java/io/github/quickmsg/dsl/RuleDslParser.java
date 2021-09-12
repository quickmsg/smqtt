package io.github.quickmsg.dsl;

import io.github.quickmsg.common.config.BootstrapConfig;
import io.github.quickmsg.rule.RuleChain;

/**
 * @author luxurong
 */
public class RuleDslParser {

    private RuleChain ruleChain = RuleChain.INSTANCE;


    private final BootstrapConfig.SmqttConfig smqttConfig;

    public RuleDslParser(BootstrapConfig.SmqttConfig smqttConfig) {
        this.smqttConfig = smqttConfig;
    }

    public RuleDslExecutor parseRule() {
        smqttConfig.getRules().forEach(ruleChain::addRule);
        return new RuleDslExecutor(ruleChain);
    }


}
