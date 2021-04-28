package io.github.quickmsg.core.minitor;

import io.github.quickmsg.common.monitor.Monitor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.lang.reflect.Field;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author luxurong
 */
@Slf4j
public class DirectUsedMonitor implements Monitor {

    public void startMonitor() {
        Flux.interval(Duration.ofSeconds(1))
                .subscribe(index -> {
                    try {
                        Thread.sleep(1000);
                        log.info("java 直接内存使用："+javaUsedDirectMemory());
                        log.info("netty 直接内存使用："+nettyUsedDirectMemory());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }                });

    }


    private long nettyUsedDirectMemory() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        Class c = Class.forName("io.netty.util.internal.PlatformDependent");
        Field field1 = c.getDeclaredField("MAX_DIRECT_MEMORY");
        field1.setAccessible(true);
        Field field2 = c.getDeclaredField("DIRECT_MEMORY_COUNTER");
        field2.setAccessible(true);
        synchronized (c) {
            Long max = (Long) field1.get(null);
            AtomicLong reserve = (AtomicLong) field2.get(null);
            return reserve.get();
        }
    }


    private long javaUsedDirectMemory() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        Class c = Class.forName("java.nio.Bits");

        Field field1 = c.getDeclaredField("maxMemory");
        field1.setAccessible(true);
        Field field2 = c.getDeclaredField("reservedMemory");
        field2.setAccessible(true);
        synchronized (c) {
            Long max = (Long) field1.get(null);
            AtomicLong reserve = (AtomicLong) field2.get(null);
            return reserve.get();
        }
    }

}
