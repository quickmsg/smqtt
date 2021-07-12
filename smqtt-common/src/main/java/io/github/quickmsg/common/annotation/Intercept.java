package io.github.quickmsg.common.annotation;

import java.lang.annotation.*;

/**
 * @author luxurong
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Intercept {

}
