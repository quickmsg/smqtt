package com.github.smqtt.core.protocol;

import com.github.smqtt.common.auth.BasicAuthentication;
import com.github.smqtt.common.channel.MqttChannel;
import com.github.smqtt.common.protocol.Protocol;
import io.netty.handler.codec.mqtt.MqttConnectMessage;
import io.netty.handler.codec.mqtt.MqttConnectPayload;
import io.netty.handler.codec.mqtt.MqttConnectVariableHeader;
import io.netty.handler.codec.mqtt.MqttMessageType;
import reactor.core.publisher.Mono;
import reactor.util.context.ContextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author luxurong
 * @date 2021/3/29 14:05
 * @description
 */
public class ConnectProtocol implements Protocol<MqttConnectMessage> {

    private static List<MqttMessageType> MESSAGE_TYPE_LIST = new ArrayList<>();

    private BasicAuthentication basicAuthentication;

    static {
        MESSAGE_TYPE_LIST.add(MqttMessageType.CONNECT);
    }

    @Override
    public Mono<Void> parseProtocol(MqttConnectMessage message, MqttChannel mqttChannel, ContextView contextView) {
        return Mono.create(monoSink -> {
            MqttConnectVariableHeader mqttConnectVariableHeader = message.variableHeader();
            MqttConnectPayload mqttConnectPayload = message.payload();
            String clientId = mqttConnectPayload.clientIdentifier();
            mqttChannel.setDeviceId(clientId);

            mqttConnectVariableHeader.
                    MqttConnectPayload mqttConnectPayload = mqttConnectVariableHeader.payload();
            RsocketChannelManager channelManager = serverConfig.getChannelManager();
            monoSink.success();
        });
    }

    @Override
    public List<MqttMessageType> getMqttMessageTypes() {
        return MESSAGE_TYPE_LIST;
    }


}
