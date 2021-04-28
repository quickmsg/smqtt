package io.github.quickmsg.common.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author luxurong
 */
@AllArgsConstructor
@Getter
public class SslContext {

    private String crt;

    private String key;


}
