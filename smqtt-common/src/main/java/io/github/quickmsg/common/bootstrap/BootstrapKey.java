package io.github.quickmsg.common.bootstrap;

/**
 * @author luxurong
 */
public class BootstrapKey {


    public final static String BOOTSTRAP_LOGGER_LEVEL = " smqtt.log.level";

    public final static String BOOTSTRAP_PORT = "smqtt.tcp.port";

    public final static String BOOTSTRAP_LOW_WATERMARK = "smqtt.tcp.lowWaterMark";

    public final static String BOOTSTRAP_HIGH_WATERMARK = "smqtt.tcp.highWaterMark";

    public final static String BOOTSTRAP_SSL = "smqtt.tcp.ssl";

    public final static String BOOTSTRAP_SSL_CRT = "smqtt.tcp.ssl.crt";

    public final static String BOOTSTRAP_SSL_KEY = "smqtt.tcp.ssl.key";

    public final static String BOOTSTRAP_WIRETAP = "smqtt.tcp.wiretap";

    public final static String BOOTSTRAP_BOSS_THREAD_SIZE = "smqtt.tcp.bossThreadSize";

    public final static String BOOTSTRAP_WORK_THREAD_SIZE = "smqtt.tcp.workThreadSize";

    public final static String BOOTSTRAP_WEB_SOCKET_PORT = "smqtt.websocket.port";

    public final static String BOOTSTRAP_WEB_SOCKET_ENABLE = "smqtt.websocket.enable";

    public final static String BOOTSTRAP_USERNAME = "smqtt.tcp.username";

    public final static String BOOTSTRAP_PASSWORD = "smqtt.tcp.password";

    public final static String BOOTSTRAP_HTTP_ENABLE = "smqtt.http.enable";

    public final static String BOOTSTRAP_HTTP_PORT = "smqtt.http.port";

    public final static String BOOTSTRAP_HTTP_ACCESS_LOG = "smqtt.http.accesslog";

    public final static String BOOTSTRAP_HTTP_SSL_ENABLE = "smqtt.http.ssl.enable";

    public final static String BOOTSTRAP_HTTP_SSL_CRT = "smqtt.http.ssl.crt";

    public final static String BOOTSTRAP_HTTP_SSL_KEY = "smqtt.http.ssl.key";

    public final static String BOOTSTRAP_CLUSTER_ENABLE = "smqtt.cluster.enable";

    public final static String BOOTSTRAP_CLUSTER_URL = "smqtt.cluster.url";

    public final static String BOOTSTRAP_CLUSTER_PORT = "smqtt.cluster.port";

    public final static String BOOTSTRAP_CLUSTER_NODE = "smqtt.cluster.node";
    /*数据库配置参数前缀*/
    public static final String DB_PREFIX = "db.";


    public class Redis {
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

    public class RedisCluster {
        public static final String REDIS_CLUSTER_SCAN_INTERVAL = "redis.cluster.scan.interval";

        public static final String REDIS_CLUSTER_NODES = "redis.cluster.nodes";

        public static final String REDIS_CLUSTER_READ_MODE = "redis.cluster.read.mode";

        public static final String REDIS_CLUSTER_RETRY_ATTEMPTS = "redis.cluster.retry.attempts";

        public static final String REDIS_CLUSTER_SLAVE_CONNECTION_POOL_SIZE = "redis.cluster.slave.connection.pool.size";

        public static final String REDIS_CLUSTER_MASTER_CONNECTION_POOL_SIZE = "redis.cluster.master.connection.pool.size";

        public static final String REDIS_CLUSTER_RETRY_INTERVAL = "redis.cluster.retry.interval";
    }

    public class RedisSentinel {
        public static final String REDIS_SENTINEL_MASTER = "redis.sentinel.master";

        public static final String REDIS_SENTINEL_NODES = "redis.sentinel.nodes";
    }

    public class RedisSingle {
        public static final String REDIS_SINGLE_ADDRESS = "redis.single.address";
    }

    public class RedisReadMode {
        public static final String SLAVE = "SLAVE";

        public static final String MASTER = "MASTER";

        public static final String MASTER_SLAVE = "MASTER_SLAVE";
    }

}