package io.github.quickmsg.persistent.strategy;

import io.github.quickmsg.common.bootstrap.BootstrapKey;
import io.github.quickmsg.persistent.message.SessionMessageEntity;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;

import java.util.*;


/**
 * 哨兵客户端策略
 *
 * @author zhaopeng
 * @date 2021/06/21
 */
public class SentinelClientStrategy implements ClientStrategy {

    /**
     * 获取redisson客户端
     *
     * @return {@link RedissonClient}
     */
    @Override
    public RedissonClient getRedissonClient(Map<String, String> environments) {
        Config config = new Config();

        String[] nodes = environments.get(BootstrapKey.RedisSentinel.REDIS_SENTINEL_NODES).split(",");
        List<String> newNodes = new ArrayList(nodes.length);

        Arrays.stream(nodes).forEach((index) -> newNodes.add(
                index.startsWith("redis://") ? index : "redis://" + index));

        SentinelServersConfig serverConfig = config.useSentinelServers()
                .addSentinelAddress(newNodes.toArray(new String[0]))
                .setDatabase(Integer.parseInt(environments.get(BootstrapKey.Redis.REDIS_DATABASE)))
                .setTimeout(Integer.parseInt(environments.get(BootstrapKey.Redis.REDIS_TIMEOUT)))
                .setConnectTimeout(Integer.parseInt(environments.get(BootstrapKey.Redis.REDIS_POOL_CONN_TIMEOUT)))
                .setMasterName(environments.get(BootstrapKey.RedisSentinel.REDIS_SENTINEL_MASTER))
                .setTimeout(Integer.parseInt(environments.get(BootstrapKey.Redis.REDIS_TIMEOUT)));

        if (StringUtils.isNotBlank(environments.get(BootstrapKey.Redis.REDIS_PASSWORD))) {
            serverConfig.setPassword(environments.get(BootstrapKey.Redis.REDIS_PASSWORD));
        }

        return Redisson.create(config);
    }
}
