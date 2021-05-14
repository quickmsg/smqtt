package io.github.quickmsg.common.http.annotation;

import io.github.quickmsg.common.http.enums.HttpType;

import java.lang.annotation.*;

/**
 * @author luxurong
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Router {

    String value();

    HttpType type() default HttpType.GET;

//    ContextType contextType();

}
