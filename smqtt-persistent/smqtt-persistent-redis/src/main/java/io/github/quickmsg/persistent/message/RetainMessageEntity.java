package io.github.quickmsg.persistent.message;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 保留消息实体
 *
 * @author zhaopeng
 * @date 2021/06/21
 */
@Data
@Builder
public class RetainMessageEntity implements Serializable {

    private static final long serialVersionUID = 1095608914696359394L;

    private String topic;

    private Integer qos;

    private byte[] body;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
