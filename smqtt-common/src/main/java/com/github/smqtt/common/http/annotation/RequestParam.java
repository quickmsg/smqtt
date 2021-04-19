package com.github.smqtt.common.http.annotation;

import java.lang.annotation.*;

/**
 * @author luxurong
 * @date 2021/4/17 01:46
 * @description
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface  RequestParam {

    String  value();
}
