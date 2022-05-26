package io.github.quickmsg.common.acl;


import lombok.Getter;

/**
 * @author luxurong
 */
public enum AclType {

    ALL(""),


    ALLOW("allow"),

    DENY("deny");

    @Getter
    private final String desc;

    AclType(String desc) {
        this.desc = desc;
    }

    public static AclType fromDesc(String desc) {
        for (AclType type : AclType.values()) {
            if (type.desc.equals(desc)) {
                return type;
            }
        }
        return null;
    }


}
