package io.github.quickmsg.persistent.strategy;

import io.github.quickmsg.common.config.BootstrapConfig;
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
     * @param redisConfig
     * @return {@link RedissonClient}
     */
    RedissonClient getRedissonClient(BootstrapConfig.RedisConfig redisConfig);
}
