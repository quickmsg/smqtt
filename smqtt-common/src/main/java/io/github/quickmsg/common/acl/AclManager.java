package io.github.quickmsg.common.acl;

/**
 * @author luxurong
 */
public interface AclManager {

    boolean auth(String sub, String source, AclAction action);

    boolean add(String sub, String source, AclAction action);

    boolean delete(String sub, String source, AclAction action);

}
