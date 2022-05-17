package io.github.quickmsg.core.ssl;

import io.github.quickmsg.common.config.Configuration;
import io.github.quickmsg.common.config.SslContext;
import io.github.quickmsg.core.mqtt.MqttConfiguration;
import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import reactor.netty.tcp.SslProvider;
import reactor.netty.tcp.TcpServer;
import reactor.netty.tcp.TcpSslContextSpec;

import java.io.File;
import java.util.Map;

/**
 * @author luxurong
 */
@Slf4j
public class AbstractSslHandler {

    public void secure(SslProvider.SslContextSpec sslContextSpec, Configuration configuration) {
        try {
            if (configuration.getSsl()) {
                SslContext sslContext = configuration.getSslContext();
                SslContextBuilder sslContextBuilder ;
                if (sslContext != null) {
                    sslContextBuilder = SslContextBuilder.forServer(new File(sslContext.getCrt()), new File(sslContext.getKey()));
                    if(StringUtils.isNotEmpty(sslContext.getCa())){
                        sslContextBuilder= sslContextBuilder.trustManager(new File(sslContext.getCa()));
                    }
                } else {
                    SelfSignedCertificate ssc = new SelfSignedCertificate();
                    sslContextBuilder = SslContextBuilder.forServer(ssc.certificate(),ssc.privateKey());
                }
                sslContextSpec.sslContext(sslContextBuilder);
            }
        } catch (Exception e) {
            log.error(" ssl read error", e);
        }

    }


    public TcpServer initTcpServer(MqttConfiguration mqttConfiguration) {
        TcpServer server = TcpServer.create();
        if (mqttConfiguration.getSsl()) {
            server = server.secure(sslContextSpec -> this.secure(sslContextSpec, mqttConfiguration));
        }
        if (mqttConfiguration.getOptions() != null) {
            for (Map.Entry<String, Object> entry : mqttConfiguration.getOptions().entrySet()) {
                server = server.option(ChannelOption.valueOf(entry.getKey()), entry.getValue());
            }
        }
        if (mqttConfiguration.getChildOptions() != null) {
            for (Map.Entry<String, Object> entry : mqttConfiguration.getChildOptions().entrySet()) {
                server = server.childOption(ChannelOption.valueOf(entry.getKey()), entry.getValue());
            }
        }
        server = server.metrics(true);
        return server;
    }


}
