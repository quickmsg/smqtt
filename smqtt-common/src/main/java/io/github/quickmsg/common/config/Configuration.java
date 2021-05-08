package io.github.quickmsg.common.config;

import io.github.quickmsg.common.cluster.ClusterConfig;

/**
 * @author luxurong
 */
public interface Configuration {


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
     * @return ssl配置
     */
    SslContext getSslContext();



    /**
     * 获取集群配置
     *
     * @return 配置config
     */
    ClusterConfig getClusterConfig();


}
