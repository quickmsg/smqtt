package io.github.quickmsg.common.interceptor;

import io.github.quickmsg.common.annotation.Intercept;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * @author luxurong
 */
public class InterceptorHandler implements InvocationHandler {

    private final Interceptor interceptor;

    private final Object target;

    public InterceptorHandler(Interceptor interceptor, Object target) {
        this.interceptor = interceptor;
        this.target = target;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        Intercept intercept = method.getAnnotation(Intercept.class);
        if (intercept == null) {
            return method.invoke(target, args);
        } else {
            return interceptor.intercept(new Invocation(method, target, args));
        }
    }
}
