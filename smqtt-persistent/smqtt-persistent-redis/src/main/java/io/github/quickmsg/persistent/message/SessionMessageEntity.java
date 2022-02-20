package io.github.quickmsg.persistent.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 会话消息实体
 *
 * @author zhaopeng
 */
@Data
@Builder
public class SessionMessageEntity implements Serializable {

    private static final long serialVersionUID = -2402958163229390974L;
    private String topic;

    private String clientId;

    private Integer qos;

    private Boolean retain;

    private String userProperties;
    
    private byte[] body;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
