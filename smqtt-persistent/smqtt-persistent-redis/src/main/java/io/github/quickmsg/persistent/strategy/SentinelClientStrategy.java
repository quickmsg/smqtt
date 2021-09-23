package io.github.quickmsg.persistent.strategy;

import io.github.quickmsg.common.bootstrap.BootstrapKey;
import io.github.quickmsg.common.config.BootstrapConfig;
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
 */
public class SentinelClientStrategy implements ClientStrategy {



    @Override
    public RedissonClient getRedissonClient(BootstrapConfig.RedisConfig redisConfig) {
        Config config = new Config();

        String[] nodes = redisConfig.getSentinel().getNodes().split(",");
        List<String> newNodes = new ArrayList(nodes.length);

        Arrays.stream(nodes).forEach((index) -> newNodes.add(
                index.startsWith("redis://") ? index : "redis://" + index));

        SentinelServersConfig serverConfig = config.useSentinelServers()
                .addSentinelAddress(newNodes.toArray(new String[0]))
                .setDatabase(redisConfig.getDatabase())
                .setTimeout(redisConfig.getTimeout())
                .setConnectTimeout(redisConfig.getPoolConnTimeout())
                .setMasterName(redisConfig.getSentinel().getMaster())
                .setTimeout(redisConfig.getTimeout());

        if (StringUtils.isNotBlank(redisConfig.getPassword())) {
            serverConfig.setPassword(redisConfig.getPassword());
        }

        return Redisson.create(config);
    }
}
