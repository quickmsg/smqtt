package io.github.quickmsg.persistent.strategy;

import io.github.quickmsg.common.bootstrap.BootstrapKey;
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
    public RedissonClient getRedissonClient(Map<String, String> environments) {
        Config config = new Config();
        String[] nodes = environments.get(BootstrapKey.RedisCluster.REDIS_CLUSTER_NODES).split(",");
        List<String> newNodes = new ArrayList(nodes.length);
        Arrays.stream(nodes).forEach((index) -> newNodes.add(index.startsWith("redis://") ? index : "redis://" + index));

        ClusterServersConfig serverConfig = config.useClusterServers()
                .addNodeAddress(newNodes.toArray(new String[0]))
                .setTimeout(Integer.parseInt(environments.get(BootstrapKey.Redis.REDIS_TIMEOUT)))
                .setConnectTimeout(Integer.parseInt(environments.get(BootstrapKey.Redis.REDIS_POOL_CONN_TIMEOUT)))
                .setScanInterval(Integer.parseInt(environments.get(BootstrapKey.RedisCluster.REDIS_CLUSTER_SCAN_INTERVAL)))
                .setReadMode(getReadMode(environments.get(BootstrapKey.RedisCluster.REDIS_CLUSTER_READ_MODE)))
                .setRetryAttempts(Integer.parseInt(environments.get(BootstrapKey.RedisCluster.REDIS_CLUSTER_RETRY_ATTEMPTS)))
                .setMasterConnectionPoolSize(Integer.parseInt(environments.get(BootstrapKey.RedisCluster.REDIS_CLUSTER_MASTER_CONNECTION_POOL_SIZE)))
                .setSlaveConnectionPoolSize(Integer.parseInt(environments.get(BootstrapKey.RedisCluster.REDIS_CLUSTER_SLAVE_CONNECTION_POOL_SIZE)))
                .setRetryInterval(Integer.parseInt(environments.get(BootstrapKey.RedisCluster.REDIS_CLUSTER_RETRY_INTERVAL)));

        if (StringUtils.isNotBlank(environments.get(BootstrapKey.Redis.REDIS_PASSWORD))) {
            serverConfig.setPassword(environments.get(BootstrapKey.Redis.REDIS_PASSWORD));
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
