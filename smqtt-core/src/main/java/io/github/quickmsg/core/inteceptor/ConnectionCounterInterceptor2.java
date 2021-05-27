package io.github.quickmsg.core.inteceptor;

import io.github.quickmsg.common.annotation.Intercept;
import io.github.quickmsg.common.interceptor.Interceptor;
import io.github.quickmsg.common.interceptor.Invocation;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;

/**
 * @author luxurong
 */
@Slf4j
public class ConnectionCounterInterceptor2 implements Interceptor {



    @Override
    public Object intercept(Invocation invocation) {
        try {
            System.out.println("test12");
            return invocation.proceed();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int sort() {
        return 1;
    }


}
