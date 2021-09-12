package io.github.quickmsg.common.rule;

import io.netty.handler.codec.mqtt.MqttMessageType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author luxurong
 * @date 2021/9/11 20:55
 */
public class RuleData extends HashMap<String,Object> {

    private MqttMessageType msgType;

    private String clientIdentifier;

    private Map<String,Object> contents;


    public Map<String,Object> initMap(){
        this.put("msgType",this.msgType.name());
        this.put("clientIdentifier",this.clientIdentifier);
        this.putAll(contents);
        return this;
    }



}
