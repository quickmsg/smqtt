package io.github.quickmsg.common.config;

import ch.qos.logback.classic.Level;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author luxurong
 */
@Data
public class BootstrapConfig {

    @JsonProperty("smqtt")
    private SmqttConfig smqttConfig;
    /**
     * 数据库配置
     */
    @JsonProperty("db")
    private DBConfig dbConfig;
    /**
     * redis配置
     */
    @JsonProperty("redis")
    private RedisConfig redisConfig;

    public static BootstrapConfig defaultConfig() {
        return new BootstrapConfig();
    }

    @Data
    public static class SmqttConfig {
        /**
         * sfl4j日志级别
         *
         * @see Level
         */
        private String logLevel;

        /**
         * tcp配置
         */
        @JsonProperty("tcp")
        private TcpConfig tcpConfig;

        /**
         * http配置
         */
        @JsonProperty("http")
        private HttpConfig httpConfig;

        /**
         * websocket配置
         */
        @JsonProperty("ws")
        private WebsocketConfig websocketConfig;

        /**
         * 集群配置配置
         */
        @JsonProperty("cluster")
        private ClusterConfig clusterConfig;
    }

    @Data
    public static class TcpConfig {
        /**
         * 端口
         */
        private Integer port;

        /**
         * 用户名
         */
        private String username;
        /**
         * 密码
         */
        private String password;
        /**
         * 二进制日志（需要开启root 为debug）
         */
        private Boolean wiretap;
        /**
         * 核心线程数
         */
        private Integer bossThreadSize;
        /**
         * 工作线程数
         */
        private Integer workThreadSize;
        /**
         * 高水位
         */
        private Integer lowWaterMark;
        /**
         * 低水位
         */
        private Integer highWaterMark;
        /**
         * ssl配置
         */
        @JsonProperty("ssl")
        private SslContext sslContext;
    }

    @Data
    public static class HttpConfig {
        /**
         * 开启http
         */
        private boolean enable;
        /**
         * http日志
         */
        private boolean accessLog;
        /**
         * ssl配置
         */
        @JsonProperty("ssl")
        private SslContext sslContext;
        /**
         * 管理页面配置
         */
        @JsonProperty("admin")
        private HttpAdmin httpAdmin;

    }

    @Data
    public static class WebsocketConfig {
        /**
         * 端口
         */
        private Integer port;
        /**
         * websocket path
         * mqtt.js 需要设置 /mqtt
         */
        private String path;
        /**
         * 开启ws
         */
        private boolean enable;
    }

    @Data
    public static class ClusterConfig {
        /**
         * 开启集群
         */
        private boolean enable;
        /**
         * 集群url
         */
        private String url;
        /**
         * 集群启动本地端口
         */
        private Integer port;
        /**
         * 集群名称 需要唯一
         */
        private String node;
        /**
         * 集群额外配置（主要用于容器映射）
         */
        private ClusterExternal external;
    }


    @Data
    public static class HttpAdmin {
        /**
         * 开启http管理页面
         */
        private boolean enable;
        /**
         * 用户名
         */
        private String username;
        /**
         * 密码
         */
        private String password;
    }

    @Data
    public static class ClusterExternal {
        /**
         * 本地曝光host
         */
        private String host;

        /**
         * 本地曝光port
         */
        private Integer port;
    }

    @Data
    public static class DBConfig {
        /**
         * 数据库驱动
         */
        private String driverClassName;
        /**
         * 数据库连接
         */
        private String url;
        /**
         * 数据库用户名
         */
        private String username;
        /**
         * 数据库密码
         */
        private String password;
        /**
         * 连接池初始化连接数
         */
        private String initialSize;
        /**
         * 连接池中最多支持多少个活动会话
         */
        private String maxActive;
        /**
         * 向连接池中请求连接时,超过maxWait的值后,认为本次请求失败
         */
        private String maxWait;
        /**
         * 回收空闲连接时，将保证至少有minIdle个连接
         */
        private String minIdle;
    }

    @Data
    public static class RedisConfig {

        /**
         * 单机模式：single 哨兵模式：sentinel 集群模式：cluster
         */
        private String mode;
        /**
         * 数据库
         */
        private Integer database;
        /**
         * 密码
         */
        private String password;
        /**
         * 超时时间
         */
        private Integer timeout;
        /**
         * 最小空闲数
         */
        private Integer poolMinIdle;
        /**
         * 连接超时时间(毫秒)
         */
        private Integer poolConnTimeout;
        /**
         * 连接池大小
         */
        private Integer poolSize;

        /**
         * redis单机配置
         */
        @JsonProperty("single")
        private RedisSingle redisSingle;

        /**
         * redis集群模式配置
         */
        @JsonProperty("cluster")
        private RedisCluster redisCluster;

        /**
         * redis哨兵模式配置
         */
        @JsonProperty("sentinel")
        private RedisSentinel redisSentinel;

    }

    /**
     * redis单机配置
     */
    @Data
    public static class RedisSingle {
        /**
         * 地址
         */
        private String address;
    }

    /**
     * redis集群模式配置
     */
    @Data
    public static class RedisCluster {
        private Integer scanInterval;
        private String nodes;
        private String readMode;
        private Integer retryAttempts;
        private Integer slaveConnectionPoolSize;
        private Integer masterConnectionPoolSize;
        private Integer retryInterval;
    }

    /**
     * redis哨兵模式配置
     */
    @Data
    public static class RedisSentinel {
        private String master;
        private String nodes;
    }


}
