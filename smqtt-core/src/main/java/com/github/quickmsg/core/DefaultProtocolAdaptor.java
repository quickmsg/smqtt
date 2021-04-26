package com.github.quickmsg.core;

import com.github.quickmsg.common.channel.MqttChannel;
import com.github.quickmsg.common.config.Configuration;
import com.github.quickmsg.common.context.ReceiveContext;
import com.github.quickmsg.common.protocol.Protocol;
import com.github.quickmsg.common.protocol.ProtocolAdaptor;
import com.github.quickmsg.common.spi.DynamicLoader;
import com.github.quickmsg.common.utils.MessageUtils;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageType;
import lombok.extern.slf4j.Slf4j;
import reactor.core.scheduler.Schedulers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author luxurong
 * @date 2021/3/31 15:22
 * @description
 */
@Slf4j
public class DefaultProtocolAdaptor implements ProtocolAdaptor {

    private Map<MqttMessageType, Protocol<MqttMessage>> types = new HashMap<>();

    @SuppressWarnings("unchecked")
    public DefaultProtocolAdaptor() {
        DynamicLoader.findAll(Protocol.class)
                .forEach(protocol ->
                        protocol.getMqttMessageTypes().forEach(type -> {
                            MqttMessageType t = (MqttMessageType) type;
                            types.put(t, protocol);
                        }));

    }

    @Override
    public <C extends Configuration> void chooseProtocol(MqttChannel mqttChannel, MqttMessage mqttMessage, ReceiveContext<C> receiveContext) {
        Optional.ofNullable(types.get(mqttMessage.fixedHeader().messageType()))
                .ifPresent(protocol -> protocol
                        .doParseProtocol(mqttMessage, mqttChannel)
                        .contextWrite(context -> context.putNonNull(ReceiveContext.class, receiveContext))
                        .subscribeOn(Schedulers.parallel())
                        .subscribe(aVoid -> {
                        }, error -> {
                            log.error("channel {} chooseProtocol:", mqttChannel, error);
                            MessageUtils.safeRelease(mqttMessage);
                        }, () -> MessageUtils.safeRelease(mqttMessage)));
    }


}
