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
     * @return {@link SessionMessage}
     */
    List<SessionMessage> getSessionMessage(String clientIdentifier);


    /**
     * 发送连接下线后的session消息
     *
     * @param sessionMessage {@link SessionMessage}
     */
    void saveSessionMessage(SessionMessage sessionMessage);


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
     * @return {@link RetainMessage}
     */
    List<RetainMessage> getRetainMessage(String topic);


}
