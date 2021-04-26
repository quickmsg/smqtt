package com.github.quickmsg.core.http;

import com.github.quickmsg.common.config.Configuration;
import com.github.quickmsg.common.config.SslContext;
import lombok.Data;

/**
 * @author luxurong
 * @date 2021/4/19 16:44
 * @description
 */
@Data
public class HttpConfiguration implements Configuration {


    private Integer port = 0;

    private Boolean wiretap = false;

    private Boolean ssl = false;

    private SslContext sslContext;

    private Integer bossThreadSize = Runtime.getRuntime().availableProcessors();

    private Integer workThreadSize = Runtime.getRuntime().availableProcessors();

    private Boolean accessLog;


    @Override
    public Integer getLowWaterMark() {
        return 0;
    }

    @Override
    public Integer getHighWaterMark() {
        return 0;
    }
}
