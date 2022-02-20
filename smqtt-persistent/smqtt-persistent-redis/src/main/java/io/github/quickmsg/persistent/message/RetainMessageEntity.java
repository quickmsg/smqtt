package io.github.quickmsg.persistent.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 保留消息实体
 *
 * @author zhaopeng
 */
@Data
@Builder
public class RetainMessageEntity implements Serializable {

    private static final long serialVersionUID = 1095608914696359394L;

    private String topic;

    private Integer qos;

    private byte[] body;

    private String userProperties;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
