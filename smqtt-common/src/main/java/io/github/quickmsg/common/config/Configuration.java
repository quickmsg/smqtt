package io.github.quickmsg.common.config;

/**
 * @author luxurong
 */
public interface Configuration {

    ConnectModel  getConnectModel();


    /**
     * netty boss线程数
     *
     * @return boss线程数
     */
    Integer getBossThreadSize();


    /**
     * netty work线程数
     *
     * @return work线程数
     */
    Integer getWorkThreadSize();


    /**
     * 业务线程数
     *
     * @return 业务线程数
     */
    Integer getBusinessThreadSize();


    /**
     * 工作队列数
     *
     * @return 工作队列数
     */
    Integer getBusinessQueueSize();

    /**
     * 获取消息最大限制值
     *
     * @return {@link Integer}
     */
    Integer getMessageMaxSize();

    /**
     * 获取全局读写限制
     *
     * @return 工作队列数
     */
    String getGlobalReadWriteSize();

    /**
     * 获取单个channel读写限制
     *
     * @return 工作队列数
     */
    String getChannelReadWriteSize();


    /**
     * mqtt 端口
     *
     * @return 端口
     */
    Integer getPort();


    /**
     * netty 低水位
     *
     * @return 数值
     */
    Integer getLowWaterMark();


    /**
     * netty 高水位
     *
     * @return 数值
     */
    Integer getHighWaterMark();


    /**
     * 开启tcp 二进制日志
     *
     * @return 布尔
     */
    Boolean getWiretap();


    /**
     * 是否开启ssl
     *
     * @return 布尔
     */
    Boolean getSsl();

    /**
     * 获取ssl加密文件
     *
     * @return {@link SslContext}
     */
    SslContext getSslContext();


    /**
     * 获取集群配置
     *
     * @return {@link BootstrapConfig.ClusterConfig}
     */
    BootstrapConfig.ClusterConfig getClusterConfig();

    /**
     * 获取监控配置
     *
     * @return {@link BootstrapConfig.MeterConfig}
     */
    BootstrapConfig.MeterConfig getMeterConfig();







}
