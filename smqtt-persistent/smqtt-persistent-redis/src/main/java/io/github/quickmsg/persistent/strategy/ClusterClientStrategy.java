package io.github.quickmsg.persistent.strategy;

import io.github.quickmsg.common.bootstrap.BootstrapKey;
import io.github.quickmsg.common.config.BootstrapConfig;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.ReadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * redisson集群客户端策略
 *
 * @author zhaopeng
 */
public class ClusterClientStrategy implements ClientStrategy {

    @Override
    public RedissonClient getRedissonClient(BootstrapConfig.RedisConfig redisConfig) {
        Config config = new Config();
        String[] nodes = redisConfig.getRedisCluster().getNodes().split(",");
        List<String> newNodes = new ArrayList(nodes.length);
        Arrays.stream(nodes).forEach((index) -> newNodes.add(index.startsWith("redis://") ? index : "redis://" + index));

        ClusterServersConfig serverConfig = config.useClusterServers()
                .addNodeAddress(newNodes.toArray(new String[0]))
                .setTimeout(redisConfig.getTimeout())
                .setConnectTimeout(redisConfig.getPoolConnTimeout())
                .setScanInterval(redisConfig.getRedisCluster().getScanInterval())
                .setReadMode(getReadMode(redisConfig.getRedisCluster().getReadMode()))
                .setRetryAttempts(redisConfig.getRedisCluster().getRetryAttempts())
                .setMasterConnectionPoolSize(redisConfig.getRedisCluster().getMasterConnectionPoolSize())
                .setSlaveConnectionPoolSize(redisConfig.getRedisCluster().getSlaveConnectionPoolSize())
                .setRetryInterval(redisConfig.getRedisCluster().getRetryInterval());

        if (StringUtils.isNotBlank(redisConfig.getPassword())) {
            serverConfig.setPassword(redisConfig.getPassword());
        }
        return Redisson.create(config);
    }

    /**
     * 读取模式
     *
     * @param readMode 模式
     * @return {@link ReadMode}
     */
    private ReadMode getReadMode(String readMode) {
        if (StringUtils.isBlank(readMode)) {
            return ReadMode.SLAVE;
        }

        if (readMode.equals(BootstrapKey.RedisReadMode.SLAVE)) {
            return ReadMode.SLAVE;
        } else if (readMode.equals(BootstrapKey.RedisReadMode.MASTER)) {
            return ReadMode.MASTER;
        } else if (readMode.equals(BootstrapKey.RedisReadMode.MASTER_SLAVE)) {
            return ReadMode.MASTER_SLAVE;
        }
        return ReadMode.SLAVE;
    }


}
