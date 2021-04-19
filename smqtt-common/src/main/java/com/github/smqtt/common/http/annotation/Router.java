package com.github.smqtt.common.http.annotation;

import com.github.smqtt.common.http.enums.HttpType;

/**
 * @author luxurong
 * @date 2021/4/18 15:19
 * @description
 */
public @interface Router {

    String value();

    HttpType type() default HttpType.GET;

}
