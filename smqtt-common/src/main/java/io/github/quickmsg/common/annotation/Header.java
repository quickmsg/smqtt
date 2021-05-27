package io.github.quickmsg.common.annotation;

import java.lang.annotation.*;
import java.util.Map;

/**
 * @author luxurong
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Header {

    String key();

    String value();


}
