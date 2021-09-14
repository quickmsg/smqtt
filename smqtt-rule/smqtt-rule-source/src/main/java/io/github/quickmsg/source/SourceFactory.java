package io.github.quickmsg.source;

import io.github.quickmsg.common.rule.Source;

/**
 * @author luxurong
 * @date 2021/8/24 16:14
 * @description
 */
public interface SourceFactory {

    SourceLoader instance(Source source);

}
