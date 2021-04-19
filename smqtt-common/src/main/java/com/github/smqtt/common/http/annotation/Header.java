package com.github.smqtt.common.http.annotation;

import java.lang.annotation.*;

/**
 * @author luxurong
 * @date 2021/4/17 01:47
 * @description
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface  Header {

    String value();


}
