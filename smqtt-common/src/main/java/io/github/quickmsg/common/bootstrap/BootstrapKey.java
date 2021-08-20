package io.github.quickmsg.common.bootstrap;

/**
 * @author luxurong
 */
public class BootstrapKey {



    public static class Redis {
        /*redis前缀key*/
        public static final String REDIS_RETAIN_MESSAGE_PREFIX_KEY = "smqtt:retain:message:";

        /*redis前缀key*/
        public static final String REDIS_SESSION_MESSAGE_PREFIX_KEY = "smqtt:session:message:";

        /*模式*/
        public static final String REDIS_MODE = "redis.mode";

        /*数据库*/
        public static final String REDIS_DATABASE = "redis.database";

        /*密码*/
        public static final String REDIS_PASSWORD = "redis.password";

        /*超时时间*/
        public static final String REDIS_TIMEOUT = "redis.timeout";

        /*最小空闲数*/
        public static final String REDIS_POOL_MIN_IDLE = "redis.pool.min.idle";

        /*连接超时时间(毫秒)*/
        public static final String REDIS_POOL_CONN_TIMEOUT = "redis.pool.conn.timeout";

        /*连接池大小*/
        public static final String REDIS_POOL_SIZE = "redis.pool.size";
    }

    public static class RedisCluster {
        public static final String REDIS_CLUSTER_SCAN_INTERVAL = "redis.cluster.scan.interval";

        public static final String REDIS_CLUSTER_NODES = "redis.cluster.nodes";

        public static final String REDIS_CLUSTER_READ_MODE = "redis.cluster.read.mode";

        public static final String REDIS_CLUSTER_RETRY_ATTEMPTS = "redis.cluster.retry.attempts";

        public static final String REDIS_CLUSTER_SLAVE_CONNECTION_POOL_SIZE = "redis.cluster.slave.connection.pool.size";

        public static final String REDIS_CLUSTER_MASTER_CONNECTION_POOL_SIZE = "redis.cluster.master.connection.pool.size";

        public static final String REDIS_CLUSTER_RETRY_INTERVAL = "redis.cluster.retry.interval";
    }

    public static class RedisSentinel {
        public static final String REDIS_SENTINEL_MASTER = "redis.sentinel.master";

        public static final String REDIS_SENTINEL_NODES = "redis.sentinel.nodes";
    }

    public static class RedisSingle {
        public static final String REDIS_SINGLE_ADDRESS = "redis.single.address";
    }

    public static class RedisReadMode {
        public static final String SLAVE = "SLAVE";

        public static final String MASTER = "MASTER";

        public static final String MASTER_SLAVE = "MASTER_SLAVE";
    }
}