package io.github.quickmsg.http;

import io.github.quickmsg.common.rule.source.Source;
import io.github.quickmsg.common.rule.source.SourceBean;
import lombok.extern.slf4j.Slf4j;
import reactor.netty.http.client.HttpClient;

import java.util.Map;

/**
 * @author luxurong
 * @date 2021/9/15 14:07
 */
@Slf4j
public class HttpSourceBean implements SourceBean {

    private String url;

    private Integer port;

    @Override
    public Boolean support(Source source) {
        return source == Source.HTTP;
    }

    @Override
    public Boolean bootstrap(Map<String, Object> sourceParam) {
        url = sourceParam.get("url").toString();
        port = (Integer) sourceParam.get("port");
        return true;
    }

    @Override
    public void transmit(Map<String, Object> object) {
        log.info("http send msg {}", object);
        HttpClient client = HttpClient.create();
             client.get()
                .uri("https://baidu.com/")
                .responseContent()
                .aggregate()
                .asString()
                .subscribe(System.out::println );
    }

    @Override
    public void close() {

    }


}
