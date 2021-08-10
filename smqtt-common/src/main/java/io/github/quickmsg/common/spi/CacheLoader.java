package io.github.quickmsg.common.spi;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author luxurong
 */
@Slf4j
public class CacheLoader {

    public Map<Class<?>, Map<String, ?>> cacheBean = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public <T> T getBeanByType(String type, Class<T> tClass) {
        Map<String, ?> beans = cacheBean.computeIfAbsent(tClass, this::loadAll);
        return (T) beans.get(type);
    }

    private <T> Map<String, T> loadAll(Class<T> aClass) {
        ServiceLoader<T> load = ServiceLoader.load(aClass);
        return StreamSupport.stream(load.spliterator(), false)
                .collect(Collectors.toMap(this::getKey, Function.identity()));
    }

    private <T> String getKey(T t) {
        Spi spi = t.getClass().getAnnotation(Spi.class);
        if (spi == null) {
            log.warn("class {} not contain spi annotation ", t);
        }
        return spi == null ? "default" : spi.type();
    }


}
