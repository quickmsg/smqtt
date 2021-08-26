package io.github.quickmsg.source;

/**
 * @author luxurong
 * @date 2021/8/24 16:14
 * @description
 */
public interface SourceFactory {

    SourceLoader instance(Source source);

}
