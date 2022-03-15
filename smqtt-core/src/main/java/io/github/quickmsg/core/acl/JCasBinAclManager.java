package io.github.quickmsg.core.acl;

import io.github.quickmsg.common.acl.AclAction;
import io.github.quickmsg.common.acl.AclManager;
import io.github.quickmsg.common.acl.AclPolicy;
import io.github.quickmsg.common.config.AclConfig;
import io.github.quickmsg.common.acl.model.PolicyModel;
import lombok.extern.slf4j.Slf4j;
import org.casbin.adapter.JDBCAdapter;
import org.casbin.jcasbin.main.Enforcer;
import org.casbin.jcasbin.model.Model;
import org.casbin.jcasbin.persist.file_adapter.FileAdapter;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author luxurong
 */
@Slf4j
public class JCasBinAclManager implements AclManager {

    private Enforcer enforcer;

    public JCasBinAclManager(AclConfig aclConfig) {
        if (aclConfig != null) {
            Model model = new Model();
            model.addDef("r", "r", "sub, obj, act");
            model.addDef("p", "p", "sub, obj, act");
            model.addDef("e", "e", "some(where (p.eft == allow))");
            model.addDef("m", "m", "r.sub == p.sub && r.obj == p.obj && r.act == p.act");
            if (aclConfig.getAclPolicy() == AclPolicy.JDBC) {
                AclConfig.JdbcAclConfig jdbcAclConfig = aclConfig.getJdbcAclConfig();
                Objects.requireNonNull(jdbcAclConfig);
                try {
                    enforcer = new Enforcer(model, new JDBCAdapter(jdbcAclConfig.getDriver(), jdbcAclConfig.getUrl(), jdbcAclConfig.getUsername(), jdbcAclConfig.getPassword()));
                } catch (Exception e) {
                    log.error("init acl jdbc error {}", aclConfig, e);
                }
            } else if (aclConfig.getAclPolicy() == AclPolicy.FILE) {
                enforcer = new Enforcer(model, new FileAdapter(aclConfig.getFilePath()));
            }
        }
    }

    @Override
    public boolean auth(String sub, String source, AclAction action) {
        return Optional.ofNullable(enforcer)
                .map(ef -> enforcer.enforce(sub, source, action.name()))
                .orElse(true);
    }

    @Override
    public boolean add(String sub, String source, AclAction action) {
        return Optional.ofNullable(enforcer)
                .map(ef -> enforcer.addNamedPolicy("p", sub, source, action.name()))
                .orElse(true);

    }

    @Override
    public boolean delete(String sub, String source, AclAction action) {
        return Optional.ofNullable(enforcer)
                .map(ef -> enforcer.removeNamedPolicy("p", sub, source, action.name()))
                .orElse(true);
    }

    @Override
    public List<List<String>> get(PolicyModel policyModel) {
        return Optional.ofNullable(enforcer)
                .map(ef -> enforcer.getFilteredNamedPolicy("p", 0, policyModel.getSubject(), policyModel.getSource(), policyModel.getAction() == null ? "" : policyModel.getAction().name()))
                .orElse(Collections.emptyList());
    }

}
