package io.github.quickmsg.persistent.strategy;

import org.redisson.api.RedissonClient;

import java.util.Map;

/**
 * redisson客户策略
 *
 * @author zhaopeng
 * @date 2021/06/21
 */
public interface ClientStrategy {


    /**
     * 获取redisson客户端
     *
     * @return {@link RedissonClient}
     */
    RedissonClient getRedissonClient(Map<String, String> environments);
}
