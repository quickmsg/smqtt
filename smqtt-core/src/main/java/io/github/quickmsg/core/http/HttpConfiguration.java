package io.github.quickmsg.core.http;

import io.github.quickmsg.common.config.BootstrapConfig;
import io.github.quickmsg.common.config.Configuration;
import io.github.quickmsg.common.config.SslContext;
import lombok.Data;

/**
 * @author luxurong
 */
@Data
public class HttpConfiguration implements Configuration {


    private Integer port = 60000;

    private Boolean wiretap = false;

    private Boolean ssl = false;

    private SslContext sslContext;

    private Integer bossThreadSize = Runtime.getRuntime().availableProcessors();

    private Integer workThreadSize = Runtime.getRuntime().availableProcessors();

    private Boolean accessLog = false;

    private Boolean enableAdmin = false;

    private String username;

    private String password;

    private Integer messageMaxSize;

    private BootstrapConfig.MeterConfig meterConfig;


    @Override
    public Integer getBusinessThreadSize() {
        return 0;
    }

    @Override
    public Integer getBusinessQueueSize() {
        return 0;
    }

    @Override
    public String getGlobalReadWriteSize() {
        return null;
    }

    @Override
    public String getChannelReadWriteSize() {
        return null;
    }

    @Override
    public Integer getLowWaterMark() {
        return 0;
    }

    @Override
    public Integer getHighWaterMark() {
        return 0;
    }

    @Override
    public BootstrapConfig.ClusterConfig getClusterConfig() {
        throw new UnsupportedOperationException("getClusterConfig");
    }

}
