package io.github.quickmsg.exception;

/**
 * @author luxurong
 */
public class NotSupportConfigException extends RuntimeException {

    public NotSupportConfigException() {
        super("请使用properties/yaml配置文件");
    }
}
