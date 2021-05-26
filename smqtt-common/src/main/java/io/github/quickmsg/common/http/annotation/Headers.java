package io.github.quickmsg.common.http.annotation;

import java.lang.annotation.*;

/**
 * @author luxurong
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Headers {

    Header[] headers();

}
