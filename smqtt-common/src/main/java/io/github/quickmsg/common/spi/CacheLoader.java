package io.github.quickmsg.common.spi;

import io.github.quickmsg.common.annotation.Spi;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.StreamSupport;

/**
 * @author luxurong
 */
@Slf4j
public class CacheLoader {

    public static Map<Class<?>, Map<String, ?>> cacheBean = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> T loadSpiByType(String type, Class<T> tClass) {
        Map<String, ?> beans = cacheBean.computeIfAbsent(tClass, CacheLoader::loadAll);
        return (T) beans.get(type);
    }

    private static Map<String, Object> loadAll(Class<?> aClass) {
        ServiceLoader<?> load = ServiceLoader.load(aClass);
        Map<String, Object> map = new HashMap<>(16);
        StreamSupport.stream(load.spliterator(), false)
                .forEach(b -> {
                    Spi spi = b.getClass().getAnnotation(Spi.class);
                    if (spi == null) {
                        log.warn("class {} not contain spi annotation ", b);
                    } else {
                        map.put(spi.type(), b);
                    }
                });
        return map;
    }


}
