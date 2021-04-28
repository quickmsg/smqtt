package com.github.quickmsg.core.ssl;

import com.github.quickmsg.common.config.Configuration;
import com.github.quickmsg.common.config.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import lombok.extern.slf4j.Slf4j;
import reactor.netty.tcp.SslProvider;

import java.io.File;

/**
 * @author luxurong
 */
@Slf4j
public class AbstractSslHandler {

    public void secure(SslProvider.SslContextSpec sslContextSpec, Configuration configuration) {
        try {
            if (configuration.getSsl()) {
                File cert;
                File key;
                SslContext sslContext = configuration.getSslContext();
                if (sslContext != null) {
                    cert = new File(sslContext.getCrt());
                    key = new File(sslContext.getKey());

                } else {
                    SelfSignedCertificate ssc = new SelfSignedCertificate();
                    cert = ssc.certificate();
                    key = ssc.privateKey();
                }
                SslContextBuilder sslContextBuilder = SslContextBuilder.forServer(cert, key);
                sslContextSpec.sslContext(sslContextBuilder);
            }

        } catch (Exception e) {
            log.error(" ssl read error", e);
        }

    }
}
