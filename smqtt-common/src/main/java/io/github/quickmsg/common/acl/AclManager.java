package io.github.quickmsg.common.acl;

import io.github.quickmsg.common.acl.model.PolicyModel;

import java.util.List;

/**
 * @author luxurong
 */
public interface AclManager {

    boolean auth(String sub, String source, AclAction action);

    boolean add(String sub, String source, AclAction action);

    boolean delete(String sub, String source, AclAction action);

    List<List<String>> get(PolicyModel policyModel);
}
