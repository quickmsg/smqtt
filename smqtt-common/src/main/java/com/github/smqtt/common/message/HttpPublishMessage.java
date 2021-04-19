package com.github.smqtt.common.message;

import lombok.Data;

import java.util.Map;

/**
 * @author luxurong
 * @date 2021/4/19 14:58
 * @description
 */
@Data
public class HttpPublishMessage {

    private String topic;

    private int qos;

    private boolean retain;

    private String message;

    private Map<String, Object> others;


}
