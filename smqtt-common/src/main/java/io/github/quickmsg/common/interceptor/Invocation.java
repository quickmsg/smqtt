package io.github.quickmsg.common.interceptor;

import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author luxurong
 * @date 2021/5/27 11:53
 * @description
 */
@AllArgsConstructor
public class Invocation {

    private Method method;

    private Object target;

    private Object[] args;

    public Object proceed() throws InvocationTargetException, IllegalAccessException {
        return method.invoke(target, args);
    }


}
