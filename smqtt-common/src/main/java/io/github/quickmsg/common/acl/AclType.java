package io.github.quickmsg.common.acl;

import lombok.Getter;

/**
 * @author luxurong
 */
public enum AclType {

    ALLOW("allow"),
    DENY("deny");

    @Getter
    private final String desc;

    AclType(String desc) {
        this.desc = desc;
    }
}
