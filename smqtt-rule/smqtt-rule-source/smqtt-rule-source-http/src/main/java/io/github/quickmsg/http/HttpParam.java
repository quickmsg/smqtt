package io.github.quickmsg.http;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author luxurong
 */
@Data
public class HttpParam {

    private String url;

    private Map<String,Object> additions;

    private Map<String, Object> headers;

}
