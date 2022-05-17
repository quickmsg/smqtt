package io.github.quickmsg.common.config;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author luxurong
 */
@Data
@NoArgsConstructor
public class SslContext {

    private String crt;

    private String key;

    private String ca;

    private Boolean enable;


    public SslContext(String crt, String key) {
        this.crt = crt;
        this.key = key;
    }

    public SslContext(String crt, String key, Boolean enable) {
        this.crt = crt;
        this.key = key;
        this.enable = enable;
    }
}
