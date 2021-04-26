package com.github.quickmsg.core.mqtt;

import com.github.quickmsg.common.auth.PasswordAuthentication;
import com.github.quickmsg.common.channel.ChannelRegistry;
import com.github.quickmsg.common.config.AbstractConfiguration;
import com.github.quickmsg.common.config.SslContext;
import com.github.quickmsg.common.message.MessageRegistry;
import com.github.quickmsg.common.protocol.ProtocolAdaptor;
import com.github.quickmsg.common.topic.TopicRegistry;
import com.github.quickmsg.core.ssl.AbstractSslHandler;
import io.netty.channel.ChannelOption;
import lombok.Data;
import reactor.netty.tcp.TcpServerConfig;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author luxurong
 * @date 2021/3/30 13:26
 * @Description MQTT协议配置类
 */
@Data
public class MqttConfiguration extends AbstractSslHandler implements AbstractConfiguration {

    private Integer port = 0;

    private Integer webSocketPort = 0;

    private Integer lowWaterMark;

    private Integer highWaterMark;

    private Boolean wiretap = false;

    private Boolean ssl = false;

    private SslContext sslContext;

    private PasswordAuthentication reactivePasswordAuth = (u, p) -> true;

    private Integer bossThreadSize = Runtime.getRuntime().availableProcessors();

    private Integer workThreadSize = Runtime.getRuntime().availableProcessors();

    private Consumer<Map<ChannelOption<?>, ?>> options;

    private Consumer<Map<ChannelOption<?>, ?>> childOptions;

    private Class<? extends ChannelRegistry> channelRegistry = ChannelRegistry.class;

    private Class<? extends TopicRegistry> topicRegistry = TopicRegistry.class;

    private Class<? extends ProtocolAdaptor> protocolAdaptor = ProtocolAdaptor.class;

    private Class<? extends MessageRegistry> messageRegistry = MessageRegistry.class;

    private Class<? extends PasswordAuthentication> passwordAuthentication = PasswordAuthentication.class;


    @Override
    public Consumer<? super TcpServerConfig> getTcpServerConfig() {
        return tcpServerConfig -> {
            Optional.ofNullable(options).ifPresent(options -> options.accept(tcpServerConfig.options()));
            Optional.ofNullable(childOptions).ifPresent(options -> options.accept(tcpServerConfig.childOptions()));
        };
    }


}
