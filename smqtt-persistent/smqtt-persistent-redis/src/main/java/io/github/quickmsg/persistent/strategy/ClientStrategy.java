package io.github.quickmsg.persistent.strategy;

import org.redisson.api.RedissonClient;

import java.util.Map;

/**
 * redisson客户策略
 *
 * @author zhaopeng
 */
public interface ClientStrategy {


    /**
     * 获取redisson客户端
     *
     * @param environments 參數map
     * @return {@link RedissonClient}
     */
    RedissonClient getRedissonClient(Map<String, String> environments);
}
