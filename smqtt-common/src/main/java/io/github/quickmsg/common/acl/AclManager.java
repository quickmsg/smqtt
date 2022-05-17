package io.github.quickmsg.common.acl;

import io.github.quickmsg.common.acl.model.PolicyModel;
import io.github.quickmsg.common.channel.MqttChannel;

import java.util.List;

/**
 * @author luxurong
 */
public interface AclManager {

    boolean check(MqttChannel mqttChannel, String source, AclAction action);

    boolean auth(String sub,String source,AclAction action);

    boolean add(String sub,String source,AclAction action,AclType type);


    boolean delete(String sub,String source,AclAction action,AclType type);

    List<List<String>> get(PolicyModel policyModel);

}
