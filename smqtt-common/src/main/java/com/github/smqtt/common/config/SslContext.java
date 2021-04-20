package com.github.smqtt.common.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author luxurong
 * @date 2021/4/20 17:00
 * @description
 */
@AllArgsConstructor
@Getter
public class SslContext {

    private String crt;

    private String key;


}
