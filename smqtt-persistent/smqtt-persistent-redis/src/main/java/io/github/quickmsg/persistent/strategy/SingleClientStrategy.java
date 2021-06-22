package io.github.quickmsg.persistent.strategy;

import io.github.quickmsg.common.bootstrap.BootstrapKey;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;

import java.util.Map;


/**
 * 单机客户端策略
 *
 * @author zhaopeng
 * @date 2021/06/21
 */
public class SingleClientStrategy implements ClientStrategy {

    /**
     * 获取redisson客户端
     *
     * @return {@link RedissonClient}
     */
    @Override
    public RedissonClient getRedissonClient(Map<String, String> environments) {
        Config config = new Config();
        String node = environments.get(BootstrapKey.RedisSingle.REDIS_SINGLE_ADDRESS);
        node = node.startsWith("redis://") ? node : "redis://" + node;
        SingleServerConfig serverConfig = config.useSingleServer()
                .setAddress(node)
                .setDatabase(Integer.parseInt(environments.get(BootstrapKey.Redis.REDIS_DATABASE)))
                .setTimeout(Integer.parseInt(environments.get(BootstrapKey.Redis.REDIS_TIMEOUT)))
                .setConnectionMinimumIdleSize(Integer.parseInt(environments.get(BootstrapKey.Redis.REDIS_POOL_MIN_IDLE)))
                .setConnectTimeout(Integer.parseInt(environments.get(BootstrapKey.Redis.REDIS_POOL_CONN_TIMEOUT)))
                .setConnectionPoolSize(Integer.parseInt(environments.get(BootstrapKey.Redis.REDIS_POOL_SIZE)));
        if (StringUtils.isNotBlank(environments.get(BootstrapKey.Redis.REDIS_PASSWORD))) {
            serverConfig.setPassword(environments.get(BootstrapKey.Redis.REDIS_PASSWORD));
        }
        return Redisson.create(config);
    }
}
