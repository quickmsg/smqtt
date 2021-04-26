package com.github.quickmsg.common.http.annotation;

import com.github.quickmsg.common.http.enums.HttpType;

import java.lang.annotation.*;

/**
 * @author luxurong
 * @date 2021/4/18 15:19
 * @description
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Router {

    String value();

    HttpType type() default HttpType.GET;

}
