package io.github.quickmsg.core.spi;

import io.github.quickmsg.common.channel.MqttChannel;
import io.github.quickmsg.common.config.Configuration;
import io.github.quickmsg.common.context.ReceiveContext;
import io.github.quickmsg.common.message.SmqttMessage;
import io.github.quickmsg.common.protocol.Protocol;
import io.github.quickmsg.common.protocol.ProtocolAdaptor;
import io.github.quickmsg.common.spi.DynamicLoader;
import io.github.quickmsg.common.utils.MessageUtils;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageType;
import lombok.extern.slf4j.Slf4j;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author luxurong
 */
@Slf4j
public class DefaultProtocolAdaptor implements ProtocolAdaptor {

    private Map<MqttMessageType, Protocol<MqttMessage>> types = new HashMap<>();


    private final Scheduler scheduler;


    @SuppressWarnings("unchecked")
    public DefaultProtocolAdaptor(Scheduler scheduler) {
        this.scheduler = Optional.ofNullable(scheduler).orElse(Schedulers.boundedElastic());
        DynamicLoader.findAll(Protocol.class)
                .forEach(protocol ->
                        protocol.getMqttMessageTypes().forEach(type -> {
                            MqttMessageType t = (MqttMessageType) type;
                            types.put(t, protocol);
                        }));

    }

    @Override
    public <C extends Configuration> void chooseProtocol(MqttChannel mqttChannel, SmqttMessage<MqttMessage> smqttMessage, ReceiveContext<C> receiveContext) {
        MqttMessage mqttMessage = smqttMessage.getMessage();
        if (mqttMessage != null && mqttMessage.decoderResult() != null && (mqttMessage.decoderResult().isSuccess())) {
            log.info("chooseProtocol channel {} chooseProtocol message {}", mqttChannel, mqttMessage);
            Optional.ofNullable(types.get(mqttMessage.fixedHeader().messageType()))
                    .ifPresent(protocol -> protocol
                            .doParseProtocol(smqttMessage, mqttChannel)
                            .contextWrite(context -> context.putNonNull(ReceiveContext.class, receiveContext))
                            .subscribeOn(scheduler)
                            .subscribe(aVoid -> {
                            }, error -> {
                                log.error("channel {} chooseProtocol: {} error {}", mqttChannel, mqttMessage, error.getMessage());
                                MessageUtils.safeRelease(mqttMessage);
                            }, () -> MessageUtils.safeRelease(mqttMessage)));
        } else {
            log.warn("chooseProtocol {} error mqttMessage {} ", mqttChannel, mqttMessage);
        }

    }


}
