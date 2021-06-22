package io.github.quickmsg.persistent.message;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 会话消息实体
 *
 * @author zhaopeng
 * @date 2021/06/21
 */
@Data
@Builder
public class SessionMessageEntity implements Serializable {

    private static final long serialVersionUID = -2402958163229390974L;
    private String topic;

    private String clientId;

    private Integer qos;

    private Boolean retain;

    private byte[] body;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
