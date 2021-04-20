package com.github.smqtt.core.http;

import com.github.smqtt.common.config.Configuration;
import com.github.smqtt.common.config.SslContext;
import lombok.Data;

/**
 * @author luxurong
 * @date 2021/4/19 16:44
 * @description
 */
@Data
public class HttpConfiguration implements Configuration {


    private Integer port = 0;

    private Integer lowWaterMark;

    private Integer highWaterMark;

    private Boolean wiretap = false;

    private Boolean ssl = false;

    private SslContext sslContext;

    private Integer bossThreadSize = Runtime.getRuntime().availableProcessors();

    private Integer workThreadSize = Runtime.getRuntime().availableProcessors();

    private Boolean accessLog;


}
