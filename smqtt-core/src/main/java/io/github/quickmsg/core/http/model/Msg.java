package io.github.quickmsg.core.http.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * 消息
 *
 * @author zhaopeng
 * @date 2021/10/20
 */
@Data
@Builder
@ToString
public class Msg<T> {
    /**
     * 成功
     */
    private Boolean success;
    /**
     * 状态码
     */
    private Integer code;
    /**
     * 消息
     */
    private String msg;
    /**
     * 数据
     */
    private T data;
}
