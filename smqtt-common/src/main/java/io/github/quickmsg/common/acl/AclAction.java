package io.github.quickmsg.common.acl;

/**
 * @author luxurong
 */
public enum AclAction {
    /**
     * mqtt connect
     */
    CONNECT,
    /**
     * mqtt sub
     */
    SUBSCRIBE,

    /**
     * mqtt pub
     */
    PUBLISH,

    /**
     * 新增ACL配置时, 一次性添加以上三种动作
     */
    ALL
}
