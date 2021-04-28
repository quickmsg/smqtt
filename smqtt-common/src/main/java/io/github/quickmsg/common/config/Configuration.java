package io.github.quickmsg.common.config;

/**
 * @author luxurong
 * @date 2021/3/29 11:25
 * @description
 */
public interface Configuration {


    Integer getBossThreadSize();


    Integer getWorkThreadSize();


    Integer getPort();


    Integer getLowWaterMark();

    Integer getHighWaterMark();

    Boolean getWiretap();


    Boolean getSsl();

    SslContext getSslContext();


}
