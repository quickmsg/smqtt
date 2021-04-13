package com.github.smqtt.core.mqtt;

import com.github.smqtt.common.auth.PasswordAuthentication;
import com.github.smqtt.common.channel.ChannelRegistry;
import com.github.smqtt.common.config.Configuration;
import com.github.smqtt.common.message.MessageRegistry;
import com.github.smqtt.common.protocol.ProtocolAdaptor;
import com.github.smqtt.common.topic.TopicRegistry;
import io.netty.channel.ChannelOption;
import lombok.Builder;
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
public class MqttConfiguration implements Configuration {

    @Builder.Default
    private Integer port = 0;

    private Integer webSocketPort;

    private Integer lowWaterMark;

    private Integer highWaterMark;

    @Builder.Default
    private Boolean wiretap = false;

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
