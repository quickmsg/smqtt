package io.github.quickmsg.common.config;

import io.github.quickmsg.common.auth.PasswordAuthentication;
import io.github.quickmsg.common.rule.RuleChainDefinition;
import io.github.quickmsg.common.rule.RuleDefinition;
import io.github.quickmsg.common.rule.source.SourceDefinition;

import java.util.List;
import java.util.Map;

/**
 * @author luxurong
 */
public interface AbstractConfiguration extends Configuration {


    /**
     * 获取websocket端口
     *
     * @return port
     */
    Integer getWebSocketPort();

    /**
     * 获取规则引擎
     *
     * @return {@link RuleDefinition}
     */
    List<RuleChainDefinition> getRuleChainDefinitions();


    /**
     * 获取source
     *
     * @return {@link SourceDefinition}
     */
    List<SourceDefinition> getSourceDefinitions();

    /**
     * 获取acl配置
     *
     * @return {@link AclConfig}
     */
    AclConfig getAclConfig();

    /**
     * 获取环境参数
     *
     * @return {@link Map}
     */
    Map<Object, Object> getEnvironmentMap();


}

