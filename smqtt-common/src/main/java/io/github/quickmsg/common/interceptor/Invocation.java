package io.github.quickmsg.common.interceptor;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author luxurong
 */
@AllArgsConstructor
@Getter
public class Invocation {

    private Method method;

    private Object target;

    private Object[] args;

    public Object proceed() throws InvocationTargetException, IllegalAccessException {
        return method.invoke(target, args);
    }


}
