package io.github.quickmsg.core.http.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author luxurong
 */
@Data
public class LoginVm {

    @JSONField(name = "access_token")
    private String accessToken;

    @JSONField(name = "expires_in")
    private long expiresIn;



}
