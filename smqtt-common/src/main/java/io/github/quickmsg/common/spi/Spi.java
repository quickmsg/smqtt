package io.github.quickmsg.common.spi;

import java.lang.annotation.*;

/**
 * @author luxurong
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Spi {

    String type();

}
