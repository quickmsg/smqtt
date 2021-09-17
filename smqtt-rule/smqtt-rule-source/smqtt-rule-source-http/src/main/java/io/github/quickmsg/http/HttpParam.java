package io.github.quickmsg.http;

import io.github.quickmsg.common.enums.HttpType;
import lombok.Data;

import java.util.Map;

/**
 * @author luxurong
 * @date 2021/9/17 11:24
 */
@Data
public class HttpParam {

    private HttpType httpType;

    private String url;

    private String body;

    private Map<String,Object> headers;

}
