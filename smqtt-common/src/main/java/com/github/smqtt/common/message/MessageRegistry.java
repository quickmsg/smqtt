package com.github.smqtt.common.message;

import com.github.smqtt.common.spi.DynamicLoader;

/**
 * @author luxurong
 * @date 2021/4/4 12:47
 * @description
 */
public interface MessageRegistry {

    MessageRegistry INSTANCE = DynamicLoader.findFirst(MessageRegistry.class).orElse(null);


}
