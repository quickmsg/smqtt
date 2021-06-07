package io.github.quickmsg.common.message;

import io.github.quickmsg.common.StartUp;
import io.github.quickmsg.common.spi.DynamicLoader;

import java.util.List;

/**
 * @author luxurong
 */
public interface MessageRegistry extends StartUp {

    MessageRegistry INSTANCE = DynamicLoader.findFirst(MessageRegistry.class).orElse(null);


    /**
     * 获取连接下线后的session消息
     *
     * @param clientIdentifier 设备id
     * @return {@link List<SessionMessage>}
     */
    List<SessionMessage> getSessionMessages(String clientIdentifier);


    /**
     * 发送连接下线后的session消息
     *
     * @param sessionMessage {@link SessionMessage}
     */
    void sendSessionMessages(SessionMessage sessionMessage);


    /**
     * 保留Topic保留消息
     *
     * @param retainMessage {@link RetainMessage}
     */
    void saveRetainMessage(RetainMessage retainMessage);


    /**
     * 保留Topic保留消息
     *
     * @param topic topic
     * @return {@link List<RetainMessage>}
     */
    List<RetainMessage> getRetainMessage(String topic);


}
