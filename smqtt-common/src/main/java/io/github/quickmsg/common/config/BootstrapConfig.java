package io.github.quickmsg.common.config;

import ch.qos.logback.classic.Level;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.quickmsg.common.auth.PasswordAuthentication;
import io.github.quickmsg.common.metric.MeterType;
import io.github.quickmsg.common.rule.RuleChainDefinition;
import io.github.quickmsg.common.rule.source.SourceDefinition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author luxurong
 */
@Data
public class BootstrapConfig {

    @JsonProperty("smqtt")
    private SmqttConfig smqttConfig;

    public static BootstrapConfig defaultConfig() {
        BootstrapConfig bootstrapConfig = new BootstrapConfig();
        SmqttConfig smqttConfig = new SmqttConfig();
        TcpConfig tcpConfig = new TcpConfig();
        tcpConfig.setPort(1883);
        smqttConfig.setTcpConfig(tcpConfig);
        smqttConfig.setLogLevel("INFO");
        bootstrapConfig.setSmqttConfig(smqttConfig);
        smqttConfig.setClusterConfig(ClusterConfig.builder()
                .enable(false).build());
        smqttConfig.setHttpConfig(HttpConfig.builder()
                .enable(false).build());
        smqttConfig.setWebsocketConfig(WebsocketConfig.builder()
                .enable(false).build());
        return bootstrapConfig;
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


        /**
         * 数据库配置
         */
        @JsonProperty("db")
        private DatabaseConfig databaseConfig;
        /**
         * redis配置
         */
        @JsonProperty("redis")
        private RedisConfig redisConfig;
        /**
         * 规则定义
         */
        @JsonProperty("rules")
        private List<RuleChainDefinition> ruleChainDefinitions;

        /**
         * 规则定义
         */
        @JsonProperty("sources")
        private List<SourceDefinition> ruleSources;

        /**
         * 指标配置
         */
        @JsonProperty("meter")
        private MeterConfig meterConfig;

        /**
         * acl配置
         */
        @JsonProperty("acl")
        private AclConfig acl;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TcpConfig {

        @Builder.Default
        private ConnectModel connectModel = ConnectModel.UNIQUE;

        /**
         * 不互踢时间 默认60s
         */
        @Builder.Default
        private Integer notKickSecond = 60;
        /**
         * 端口
         */
        private Integer port;
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
         * 业务线程数
         */
        private Integer businessThreadSize;

        /**
         * 业务队列
         */
        private Integer businessQueueSize;

        /**
         * 接收消息的最大限制
         */
        private Integer messageMaxSize;

        /**
         * 低水位
         */
        private Integer lowWaterMark;

        /**
         * 高水位
         */
        private Integer highWaterMark;
        /**
         * ssl配置
         */
        private SslContext ssl;

        /**
         * 全局写字节限制
         */
        private String globalReadWriteSize;

        /**
         * 单个连接读写字节限制
         */
        private String channelReadWriteSize;


        /**
         * server channel options
         */
        Map<String, Object> options;

        /**
         * child client channel options
         */
        Map<String, Object> childOptions;

        /**
         * PasswordAuthentication
         */
        PasswordAuthentication authentication;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
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
        private SslContext ssl;
        /**
         * 管理页面配置
         */
        private HttpAdmin admin;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
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
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
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
         * 集群空间 需要一致才能通信
         */
        private String namespace;
        /**
         * 集群额外配置（主要用于容器映射）
         */
        private ClusterExternal external;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DatabaseConfig {
        /**
         * 数据库连接
         */
        private String jdbcUrl;
        /**
         * 用户名
         */
        private String username;
        /**
         * 密码
         */
        private String password;

        private Boolean dataSourceCachePrepStmts;
        private Integer dataSourcePrepStmtCacheSize;
        private Integer dataSourcePrepStmtCacheSqlLimit;
        private Boolean dataSourceUseServerPrepStmts;
        private Boolean dataSourceUseLocalSessionState;
        private Boolean dataSourceRewriteBatchedStatements;
        private Boolean dataSourceCacheResultSetMetadata;
        private Boolean dataSourceCacheServerConfiguration;
        private Boolean dataSourceElideSetAutoCommits;
        private Boolean dataSourceMaintainTimeStats;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
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
        private RedisSingle single;

        /**
         * redis集群模式配置
         */
        private RedisCluster cluster;

        /**
         * redis哨兵模式配置
         */
        private RedisSentinel sentinel;

    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
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
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
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


    /**
     * redis单机配置
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
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
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
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
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RedisSentinel {
        private String master;
        private String nodes;
    }

    /**
     * 指标配置
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MeterConfig {

        private MeterType meterType;

        private Influxdb influxdb;
    }

    /**
     * influx1.x参数
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Influxdb {
        /**
         * 数据库
         */
        private String db;
        /**
         * uri
         */
        private String uri;
        /**
         * 用户名
         */
        private String userName;
        /**
         * 密码
         */
        private String password;
        /**
         * 步长
         */
        private int step;
    }
}
