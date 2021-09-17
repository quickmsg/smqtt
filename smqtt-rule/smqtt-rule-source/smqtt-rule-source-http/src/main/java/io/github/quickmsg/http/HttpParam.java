package io.github.quickmsg.http;

import lombok.Data;

import java.util.Map;

/**
 * @author luxurong
 * @date 2021/9/17 11:24
 */
@Data
public class HttpParam {

    private String url;

    private Map<String,Object> additions;

    private Map<String,Object> headers;

}
