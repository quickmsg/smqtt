package io.github.quickmsg.core.http;

import io.github.quickmsg.common.cluster.ClusterConfig;
import io.github.quickmsg.common.config.Configuration;
import io.github.quickmsg.common.config.SslContext;
import lombok.Data;

/**
 * @author luxurong
 */
@Data
public class HttpConfiguration implements Configuration {


    private Integer port = 0;

    private Boolean wiretap = false;

    private Boolean ssl = false;

    private SslContext sslContext;

    private Integer bossThreadSize = Runtime.getRuntime().availableProcessors();

    private Integer workThreadSize = Runtime.getRuntime().availableProcessors();

    private Boolean accessLog = false;

    private Boolean enableAdmin = false;

    private String username;

    private String password;


    @Override
    public Integer getBusinessThreadSize() {
        return 0;
    }

    @Override
    public Integer getBusinessQueueSize() {
        return 0;
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
    public ClusterConfig getClusterConfig() {
        throw new UnsupportedOperationException("getClusterConfig");
    }

}
