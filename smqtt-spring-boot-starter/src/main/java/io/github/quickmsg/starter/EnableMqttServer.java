package io.github.quickmsg.starter;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author luxurong
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(AutoMqttConfiguration.class)
@Documented
@EnableAutoConfiguration
public @interface EnableMqttServer {



}
