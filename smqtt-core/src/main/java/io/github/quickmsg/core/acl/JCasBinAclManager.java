package io.github.quickmsg.core.acl;

import io.github.quickmsg.common.acl.AclAction;
import io.github.quickmsg.common.acl.AclManager;
import io.github.quickmsg.common.acl.AclPolicy;
import io.github.quickmsg.common.acl.AclType;
import io.github.quickmsg.common.acl.filter.AclFunction;
import io.github.quickmsg.common.acl.model.PolicyModel;
import io.github.quickmsg.common.channel.MqttChannel;
import io.github.quickmsg.common.config.AclConfig;
import io.github.quickmsg.common.utils.TopicRegexUtils;
import lombok.extern.slf4j.Slf4j;
import org.casbin.adapter.JDBCAdapter;
import org.casbin.jcasbin.main.Enforcer;
import org.casbin.jcasbin.model.Model;
import org.casbin.jcasbin.persist.file_adapter.FileAdapter;
import org.casbin.jcasbin.util.BuiltInFunctions;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author luxurong
 */
@Slf4j
public class JCasBinAclManager implements AclManager {

    private Enforcer enforcer;

    private final Map<String, Set<String>> filterAclTopicActions = new ConcurrentHashMap<>();

    private final String REQUEST_SUBJECT_TEMPLATE = "%s:%s";

    public JCasBinAclManager(AclConfig aclConfig) {
        if (aclConfig != null) {
            Model model = new Model();
            model.addDef("r", "r", "sub, obj, act");
            model.addDef("p", "p", " sub, obj, act, eft");
            model.addDef("g", "g", "_, _");
            model.addDef("e", "e", "some(where (p.eft == allow)) && !some(where (p.eft == deny))");
            model.addDef("m", "m", "r.act == p.act && keyMatch2(r.obj,p.obj)  && filter(r.sub, p.sub)");
            if (aclConfig.getAclPolicy() == AclPolicy.JDBC) {
                AclConfig.JdbcAclConfig jdbcAclConfig = aclConfig.getJdbcAclConfig();
                Objects.requireNonNull(jdbcAclConfig);
                try {
                    enforcer = new Enforcer(model, new JDBCAdapter(jdbcAclConfig.getDriver(), jdbcAclConfig.getUrl(),
                            jdbcAclConfig.getUsername(), jdbcAclConfig.getPassword()));
                } catch (Exception e) {
                    log.error("init acl jdbc error {}", aclConfig, e);
                }
            } else if (aclConfig.getAclPolicy() == AclPolicy.FILE) {
                enforcer = new Enforcer(model, new FileAdapter(aclConfig.getFilePath()));
            } else {
                enforcer = new Enforcer();
            }
            enforcer.addFunction("filter", new AclFunction());
            List<String> objects = enforcer.getAllObjects();
            List<String> actions = enforcer.getAllActions();
            for (int i = 0; i < objects.size(); i++) {
                Set<String> allObjects = filterAclTopicActions.computeIfAbsent(actions.get(i), a -> new HashSet<>());
                allObjects.add(objects.get(i));
            }
        }
    }

    @Override
    public boolean check(MqttChannel mqttChannel, String source, AclAction action) {
        try {
            boolean isCheckAcl = Optional.ofNullable(filterAclTopicActions.get(action.name()))
                    .map(objects -> objects.stream().anyMatch(topic->BuiltInFunctions.keyMatch2(source,topic)))
                    .orElse(false);
            if (isCheckAcl) {
                String subject = String.format(REQUEST_SUBJECT_TEMPLATE, mqttChannel.getClientIdentifier()
                        , mqttChannel.getAddress().split(":")[0]);
                return Optional.ofNullable(enforcer)
                        .map(ef -> ef.enforce(subject, source, action.name()))
                        .orElse(true);
            }

        } catch (Exception e) {
            log.error("acl check error",e);
        }
        return true;
    }

    @Override
    public boolean add(String sub, String source, AclAction action, AclType type) {
        return Optional.ofNullable(enforcer)
                .map(ef -> enforcer.addNamedPolicy("p", sub, source, action.name(),type.getDesc()))
                .orElse(true);

    }

    @Override
    public boolean delete(String sub, String source, AclAction action,AclType type) {
        return Optional.ofNullable(enforcer)
                .map(ef -> enforcer.removeNamedPolicy("p", sub, source, action.name(),type.getDesc()))
                .orElse(true);
    }

    @Override
    public List<List<String>> get(PolicyModel policyModel) {
        return Optional.ofNullable(enforcer)
                .map(ef -> enforcer
                        .getFilteredNamedPolicy("p", 0,
                                policyModel.getSubject(), policyModel.getSource(),
                                policyModel.getAction() == null || AclAction.ALL == policyModel.getAction() ? "" : policyModel.getAction().name(),
                                policyModel.getAclType()==null || AclType.ALL == policyModel.getAclType()  ?"":policyModel.getAclType().getDesc())
                        )
                .orElse(Collections.emptyList());
    }

}
