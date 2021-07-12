package io.github.quickmsg.common.interceptor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * @author luxurong
 */
@AllArgsConstructor
@Getter
@Slf4j
public class Invocation {

    private Method method;

    private Object target;

    private Object[] args;

    public Object proceed() {
        try {
            return method.invoke(target, args);
        } catch (Exception e) {
            log.error("invocation {} proceed error", this, e);
            return null;
        }
    }


}
