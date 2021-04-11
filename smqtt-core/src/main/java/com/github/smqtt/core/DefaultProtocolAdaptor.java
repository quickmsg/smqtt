package com.github.smqtt.core;

import com.github.smqtt.common.channel.MqttChannel;
import com.github.smqtt.common.config.Configuration;
import com.github.smqtt.common.context.ReceiveContext;
import com.github.smqtt.common.protocol.Protocol;
import com.github.smqtt.common.protocol.ProtocolAdaptor;
import com.github.smqtt.common.spi.DynamicLoader;
import io.netty.buffer.ByteBuf;
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
//       log.info("channel {} message {}",mqttChannel,mqttMessage);
        Optional.ofNullable(types.get(mqttMessage.fixedHeader().messageType()))
                .ifPresent(protocol -> protocol.doParseProtocol(mqttMessage, mqttChannel)
                        .contextWrite(context -> context.putNonNull(ReceiveContext.class, receiveContext))
                        .subscribeOn(Schedulers.parallel())
                        .subscribe(aVoid -> {
                        }, error -> {
                            log.error("channel {} chooseProtocol:", mqttChannel, error);
                            releaseMessage(mqttMessage);
                        }, () -> releaseMessage(mqttMessage)));
    }

    private void releaseMessage(MqttMessage mqttMessage) {
        if (mqttMessage.payload() instanceof ByteBuf) {
            // safe release byteBuf
            ByteBuf byteBuf = ((ByteBuf) mqttMessage.payload());
            int count = byteBuf.refCnt();
            if (count > 0) {
                byteBuf.release(count);
                log.info("netty success release byteBuf {} count {} ", byteBuf, count);
            }
        }
    }


}
