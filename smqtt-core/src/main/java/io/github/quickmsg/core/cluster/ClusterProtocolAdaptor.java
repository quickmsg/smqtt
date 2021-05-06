package io.github.quickmsg.core.cluster;

import io.github.quickmsg.common.channel.MqttChannel;
import io.github.quickmsg.common.config.Configuration;
import io.github.quickmsg.common.context.ReceiveContext;
import io.github.quickmsg.common.spi.DynamicLoader;
import io.github.quickmsg.core.DefaultProtocolAdaptor;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttPublishMessage;

import java.util.Optional;

/**
 * @author luxurong
 */
public class ClusterProtocolAdaptor extends DefaultProtocolAdaptor {


    @Override
    public <C extends Configuration> void chooseProtocol(MqttChannel mqttChannel, MqttMessage mqttMessage, ReceiveContext<C> receiveContext) {
        if (mqttMessage instanceof MqttPublishMessage) {
            receiveContext.getClusterRegistry();
        }
        super.chooseProtocol(mqttChannel, mqttMessage, receiveContext);
    }


    public static void main(String[] args) {
      Optional s= DynamicLoader.findFirst(ClusterProtocolAdaptor.class);
      System.out.println(s.isPresent());
    }

}
