package io.github.quickmsg.common.config;

/**
 * @author luxurong
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
