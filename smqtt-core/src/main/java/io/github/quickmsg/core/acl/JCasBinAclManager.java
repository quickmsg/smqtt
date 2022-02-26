package io.github.quickmsg.core.acl;

import io.github.quickmsg.common.acl.AclAction;
import io.github.quickmsg.common.acl.AclManager;
import io.github.quickmsg.common.acl.AclPolicy;
import io.github.quickmsg.common.config.AclConfig;
import io.github.quickmsg.common.utils.ClassPathLoader;
import lombok.extern.slf4j.Slf4j;
import org.casbin.adapter.JDBCAdapter;
import org.casbin.jcasbin.main.Enforcer;

import java.net.URL;
import java.util.Objects;
import java.util.Optional;

/**
 * @author luxurong
 */
@Slf4j
public class JCasBinAclManager implements AclManager {

    private final AclConfig aclConfig;

    private Enforcer enforcer;

    public JCasBinAclManager(AclConfig aclConfig) {
        this.aclConfig = aclConfig;
        String rootPath = Optional.ofNullable(Thread.currentThread().getContextClassLoader().getResource("")).map(URL::getPath).orElse(null);
        if (aclConfig.getAclPolicy() == AclPolicy.JDBC) {
            AclConfig.JdbcAclConfig jdbcAclConfig = aclConfig.getJdbcAclConfig();
            Objects.requireNonNull(jdbcAclConfig);
            try {
                enforcer = new Enforcer(rootPath + "mqtt_model.conf", new JDBCAdapter(jdbcAclConfig.getDriver(), jdbcAclConfig.getUrl(), jdbcAclConfig.getUsername(), jdbcAclConfig.getPassword()));
            } catch (Exception e) {
                log.error("init acl jdbc error {}", aclConfig, e);
            }
        } else {
            enforcer = new Enforcer(rootPath + "mqtt_model.conf", aclConfig.getFilePath());
        }
    }

    @Override
    public boolean auth(String sub, String source, AclAction action) {
        if (aclConfig.getAclPolicy() == AclPolicy.NONE) {
            return true;
        } else {
            return enforcer.enforce(sub, source, action.name());
        }
    }

    @Override
    public boolean add(String sub, String source, AclAction action) {
        return enforcer.addNamedPolicy("p", sub, source, action.name());
    }

    @Override
    public boolean delete(String sub, String source, AclAction action) {
        return enforcer.removeNamedPolicy("p", sub, source, action.name());
    }
}
