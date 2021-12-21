package io.github.quickmsg.core.mqtt;

import io.github.quickmsg.common.Receiver;
import io.github.quickmsg.common.channel.MqttChannel;
import io.github.quickmsg.core.ssl.AbstractSslHandler;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.tcp.TcpServer;
import reactor.util.context.ContextView;

/**
 * @author luxurong
 */
public class MqttReceiver extends AbstractSslHandler implements Receiver {

    @Override
    public Mono<DisposableServer> bind() {
        return Mono.deferContextual(contextView -> Mono.just(this.newTcpServer(contextView)))
                .flatMap(view -> view.bind().cast(DisposableServer.class));
    }

    private TcpServer newTcpServer(ContextView context) {
        MqttReceiveContext receiveContext = context.get(MqttReceiveContext.class);
        MqttConfiguration mqttConfiguration = receiveContext.getConfiguration();
        TcpServer server = initTcpServer(mqttConfiguration);
        return server.port(mqttConfiguration.getPort())
                .wiretap(mqttConfiguration.getWiretap())
                .childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(mqttConfiguration.getLowWaterMark(), mqttConfiguration.getHighWaterMark()))
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.SO_REUSEADDR, true)
                .metrics(mqttConfiguration.getMeterConfig() != null)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .runOn(receiveContext.getLoopResources())
                .doOnConnection(connection -> {
                    connection
                            .addHandler(MqttEncoder.INSTANCE)
                            .addHandler(new MqttDecoder(mqttConfiguration.getMessageMaxSize()))
                            .addHandler(receiveContext.getTrafficHandlerLoader().get());
                    receiveContext.apply(MqttChannel.init(connection,receiveContext.getTimeAckManager()));
                });
    }
}